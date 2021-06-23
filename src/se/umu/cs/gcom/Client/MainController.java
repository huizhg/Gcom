package se.umu.cs.gcom.Client;


import se.umu.cs.gcom.GCom.*;
import javax.swing.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainController {
    private MainView mainView;
    private IGComService gComService;


    private GroupUpdate groupUpdate;
    private MemberUpdate memberUpdate;
    private MsgUpdate msgUpdate;
    private QueueUpdate queueUpdate;

    public MainController(MainView mainView) {
        this.mainView = mainView;
        loginController();
    }
    private void loginController(){
        loginButtonListener();
    }
    private void groupController(){
        try {
            groupUpdate = new GroupUpdate(gComService.getNameStub(),mainView.getGrouplistField());
            groupUpdate.execute();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        createButtonListener();
        removeButtionListener();
        joinButtionListener();
    }
    private void userController(){
        try {
            memberUpdate = new MemberUpdate(gComService.getGroupManager(),mainView.getMemberlist());
            memberUpdate.execute();
            msgUpdate = new MsgUpdate(gComService.getGroupManager(),mainView.getMessagelist(),mainView.BackendArea,mainView.PerformanceArea);
            msgUpdate.execute();

        } catch (RemoteException e) {
            e.printStackTrace();
        }


        updateMemberButtonListener();
        leaveButtonListenser();
        debugButtionListener();
        removeMButtonListener();
        addMButtonListener();

        mainView.getSend().addActionListener(e -> {
            String msgContent = mainView.getInputMessage().getText();

            try {
                Message msg = gComService.prepareMsg(MessageType.NORMAL, msgContent);
                if(mainView.getDebugframe().isVisible()){
                    mainView.msglistModel.addElement(msg);
                }else {
                    System.out.println("Multicast");
                    try {
                        gComService.multicast(msg);
                    } catch (RemoteException remoteException) {
                        remoteException.printStackTrace();
                    }
                }
            } catch (RemoteException remoteException) {
                remoteException.printStackTrace();
            }


        });
    }
    private void addMButtonListener(){
        mainView.getAddMemberButton().addActionListener(e -> {
            String memberName = (String) mainView.getInputMessage().getText();
            try {
                gComService.addMember(memberName);
            } catch (RemoteException remoteException) {
                remoteException.printStackTrace();
            }
            updateMemberList();
        });
    }
    private void removeMButtonListener(){
        mainView.getRemoveMemberButton().addActionListener(e -> {
            String memberName = (String) mainView.getMemberlist().getSelectedValue();
            try {
                gComService.removeMember(memberName);
            } catch (RemoteException remoteException) {
                remoteException.printStackTrace();
            }
            updateMemberList();
        });
    }
    private void leaveButtonListenser(){
        mainView.getLeaveButton().addActionListener(e -> {
            msgUpdate.cancel(true);
            try {
                gComService.leaveGroup();

            } catch (RemoteException remoteException) {
                remoteException.printStackTrace();
            }
            mainView.getGroupPanel().setEnabled(true);
            mainView.getGroupPanel().setVisible(true);
            mainView.getUserView().setEnabled(false);
            mainView.getUserView().setVisible(false);
            if (mainView.getDebugframe().isVisible()){
                System.exit(1);
            }
        });
    }
    private void debugButtionListener(){
        mainView.getDebugButton().addActionListener(e -> {
            String username = null;
            try {
                username = gComService.getUser().getId();
            } catch (RemoteException remoteException) {
                remoteException.printStackTrace();
            }

            mainView.builddebugView(username);

            try {
                queueUpdate = new QueueUpdate(gComService.getGroupManager().getCurrentGroup().getOrderingModule(),mainView.getDebugqueueList());
                queueUpdate.execute();
            } catch (RemoteException remoteException) {
                remoteException.printStackTrace();
            }

            mainView.getDebugsendButton().addActionListener(e1 -> {
                System.out.println("Debug Send was clicked.");
                int msgNum = mainView.getDebugmessagesList().getSelectedIndex();
                Message msg = (Message) mainView.getDebugmessagesList().getSelectedValue();
                System.out.println("Sent Msg = "+msg.toString());
                try {
                    gComService.multicast(msg);
                } catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                }
                mainView.msglistModel.remove(msgNum);
            });
            mainView.getDebugdeliverButton().addActionListener(e2 -> {
                mainView.buildbackendView();

            });
        });
    }
    private void updateMemberButtonListener(){
        mainView.getUpdateMemberButton().addActionListener(e -> {
            updateMemberList();
        });
    }
    private void updateMemberList(){
        System.out.println("Update member list");
        List<String> liveM = new ArrayList<>();
        Map<Integer, List<String>> map = null;
        try {
            map = gComService.liveCheck();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        liveM = map.get(1);

        DefaultListModel<String> mListModel = new DefaultListModel<>();
        for (String m:liveM){
            mListModel.addElement(m);
        }
        mainView.getMemberlist().setModel(mListModel);
    }
    private void joinButtionListener(){
        mainView.getJoinButton().addActionListener(e -> {
            String groupName = (String) mainView.getGrouplistField().getSelectedValue();
            joinGroupAction(groupName);
        });
    }
    private void joinGroupAction(String groupName){
        String username = "";
        try {
            String leaderId = mainView.getJoinGroupField().getText();
            gComService.joinGroup(groupName,leaderId);
            username = gComService.getUser().getId();
            System.out.println("Join Group: "+ groupName+", User: "+username);

            mainView.getGroupPanel().setEnabled(false);
            mainView.getGroupPanel().setVisible(false);
            mainView.buildUserView(username);
            userController();
        } catch (RemoteException remoteException) {
            System.out.println("Failed to join group.");
            remoteException.printStackTrace();
        } catch (NullPointerException nullPointerException){
            System.out.println("Others.");
            String leaderId = mainView.getJoinGroupField().getText();
            System.out.println(leaderId);
        }

    }
    private void removeButtionListener(){
        mainView.getRemoveButton().addActionListener(e -> {
            String groupName = (String) mainView.getGrouplistField().getSelectedValue();
            try {
                gComService.removeGroup(groupName);
                System.out.println("Remove Group: "+ groupName+", leader: "+gComService.getUser().getId());
            } catch (RemoteException remoteException) {
                System.out.println("Failed to remove group.");
            }
            updateGrouplist();
        });
    }
    private void createButtonListener(){
        mainView.getCreateButton().addActionListener(e -> {
            String groupName = mainView.getCreateGroupField().getText();

            try {
                String username = gComService.getUser().getId();
                gComService.createGroup(groupName,(String)mainView.getComTypeBox().getSelectedItem(),(String)mainView.getOrderTypeBox().getSelectedItem());
                mainView.getFrame().setTitle("GCom Chat View: User - "+username+" Last Group: "+groupName);
            } catch (RemoteException remoteException) {
                System.out.println("Failed to create group.");
                remoteException.printStackTrace();
            }

//            updateGrouplist();

        });
    }

    private void updateGrouplist() {
        List<String> groupList = new ArrayList<>();
        try {
            groupList = gComService.getAllGroups();
            System.out.println("Group list was updated.");
        } catch (RemoteException e) {
            System.out.println("Failed to update group list.");
        }
        DefaultListModel<String> groupListModel = new DefaultListModel<>();
        for (String g: groupList){
            groupListModel.addElement(g);
        }
        mainView.getGrouplistField().setModel(groupListModel);
    }

    private void loginButtonListener(){
        mainView.getLoginButton().addActionListener(e -> {
            String username = mainView.getUserText().getText();
            try {
                gComService = new GComService(username);
                System.out.println("Init user: username - "+username);
            } catch (RemoteException  remoteException) {
                remoteException.printStackTrace();
            }
            mainView.getLoginPanel().setEnabled(false);
            mainView.getLoginPanel().setVisible(false);
            mainView.buildGroupView(username);
            groupController();
        });
    }
}
