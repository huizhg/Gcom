package se.umu.cs.gcom.Debugger;


import se.umu.cs.gcom.Communication.Communication;
import se.umu.cs.gcom.Communication.NonReliableMulticast;
import se.umu.cs.gcom.GroupManagement.*;
import se.umu.cs.gcom.MessageOrdering.*;
import se.umu.cs.gcom.Naming.INamingService;

import javax.swing.*;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainController {
    private MainView mainView;
    private User user;
    private GroupManager groupManager;
    private IGComService gComService;
//    private Communication communicationMethod;
    private Ordering orderingMethod;
//    private Group group;

    private Registry registry;
    private INamingService nameStub;
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
        groupUpdate = new GroupUpdate(nameStub,mainView.getGrouplistField());
        groupUpdate.execute();
//        updateGrouplist();
        createButtonListener();
        removeButtionListener();
        joinButtionListener();
    }
    private void userController(){
        memberUpdate = new MemberUpdate(groupManager,mainView.getMemberlist());
        memberUpdate.execute();

        msgUpdate = new MsgUpdate(groupManager,mainView.getMessagelist());
        msgUpdate.execute();

        groupManager.notifyMemberJoined(user);

//        updateMemberList();
        updateMemberButtonListener();
        leaveButtonListenser();
        debugButtionListener();
        removeMButtonListener();
        addMButtonListener();
        orderingMethod = groupManager.getCurrentGroup().getOrderingMethod();

        mainView.getSend().addActionListener(e -> {
            String msgContent = mainView.getInputMessage().getText();
            Message msg = new Message(user,MessageType.NORMAL,msgContent);
            msg = orderingMethod.createMsg(msg);
//            VectorClock clock = new VectorClock();
//            clock.initialize(user);
//            clock.increment(user);
//            msg.setVectorClock(clock);
//            System.out.println("Msg was built."+groupManager.getGroupId());
            if(mainView.getDebugframe().isVisible()){
                mainView.msglistModel.addElement(msg);
//                msgUpdate.cancel(true);
            }else {
                System.out.println("Multicast");
                groupManager.multicast(msg);
            }
        });
    }
    private void addMButtonListener(){
        mainView.getAddMemberButton().addActionListener(e -> {
            String memberName = (String) mainView.getInputMessage().getText();
            try {
                groupManager.addMember(memberName);
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
                groupManager.removeMember(memberName);
            } catch (RemoteException remoteException) {
                remoteException.printStackTrace();
            }
            updateMemberList();
        });
    }
    private void leaveButtonListenser(){
        mainView.getLeaveButton().addActionListener(e -> {
            msgUpdate.cancel(true);
//            System.out.println("Out of try");
            try {
//                System.out.println("In the try");
                groupManager.leaveGroup();

            } catch (RemoteException remoteException) {
                remoteException.printStackTrace();
            }
            mainView.getGroupPanel().setEnabled(true);
            mainView.getGroupPanel().setVisible(true);
            mainView.getUserView().setEnabled(false);
            mainView.getUserView().setVisible(false);
            if (mainView.getDebugframe().isVisible()){
//                System.out.println("Dispose!");
                System.exit(1);
            }
        });
    }
    private void debugButtionListener(){
        mainView.getDebugButton().addActionListener(e -> {
            String username = user.getId();
//            System.out.println(username);

            mainView.builddebugView(username);

            queueUpdate = new QueueUpdate(orderingMethod,mainView.getDebugqueueList());
            queueUpdate.execute();

            mainView.getDebugsendButton().addActionListener(e1 -> {
                System.out.println("Debug Send was clicked.");
                Integer msgNum = mainView.getDebugmessagesList().getSelectedIndex();
//                System.out.println("Selected index = "+msgNum.toString());
                Message msg = (Message) mainView.getDebugmessagesList().getSelectedValue();
                System.out.println("Sent Msg = "+msg.toString());
                groupManager.multicast(msg);
                mainView.msglistModel.remove(msgNum);
            });
            mainView.getDebugdeliverButton().addActionListener(e2 -> {
                msgUpdate = new MsgUpdate(groupManager,mainView.getMessagelist());
                msgUpdate.execute();

            });
        });
    }
    private void updateMemberButtonListener(){
        mainView.getUpdateMemberButton().addActionListener(e -> {
//            updateMemberList();
//            try {
//                Message msg = gComService.getGroupManager().getCurrentGroup().getOrderingMethod().deliver();
//                System.out.println(msg.toString());
//            } catch (InterruptedException | RemoteException interruptedException) {
//                interruptedException.printStackTrace();
//            }


//            System.out.println("Update member list");
//            List<String> liveM = new ArrayList<>();
//            try {
//                Map<Integer, List<String>> map = groupManager.liveCheck();
//                liveM = map.get(1);
//            } catch (RemoteException remoteException) {
//                remoteException.printStackTrace();
//            }
//
//            DefaultListModel<String> mListModel = new DefaultListModel<>();
//            for (String m:liveM){
//                mListModel.addElement(m);
//            }
//            mainView.getMemberlist().setModel(mListModel);

        });
    }
    private void updateMemberList(){
        System.out.println("Update member list");
        List<String> liveM = new ArrayList<>();
        try {
            Map<Integer, List<String>> map = groupManager.liveCheck();
            liveM = map.get(1);
        } catch (RemoteException remoteException) {
            remoteException.printStackTrace();
        }

        DefaultListModel<String> mListModel = new DefaultListModel<>();
        for (String m:liveM){
            mListModel.addElement(m);
        }
        mainView.getMemberlist().setModel(mListModel);
    }
    private void joinButtionListener(){
        mainView.getJoinButton().addActionListener(e -> {
            String groupName = (String) mainView.getGrouplistField().getSelectedValue();
//            String groupName = mainView.getJoinGroupField().getText();
            joinGroupAction(groupName);
        });
    }
    private void joinGroupAction(String groupName){
        String username = "";
        try {
            groupManager.joinGroup(groupName);
            username = user.getId();
            System.out.println("Join Group: "+ groupName+", User: "+username);
            //---
//                updateGrouplist();
            mainView.getGroupPanel().setEnabled(false);
            mainView.getGroupPanel().setVisible(false);
            mainView.buildUserView(username);
            userController();
        } catch (RemoteException remoteException) {
            System.out.println("Failed to join group.");
            remoteException.printStackTrace();
        }

    }
    private void removeButtionListener(){
        mainView.getRemoveButton().addActionListener(e -> {
            String groupName = (String) mainView.getGrouplistField().getSelectedValue();
            try {
                groupManager.removeGroup(groupName);
                System.out.println("Remove Group: "+ groupName+", leader: "+user.getId());
            } catch (RemoteException remoteException) {
                System.out.println("Failed to remove group.");
            }
            updateGrouplist();
        });
    }
    private void createButtonListener(){
        mainView.getCreateButton().addActionListener(e -> {
            String groupName = mainView.getCreateGroupField().getText();

            Group createdGroup = new Group(groupName);
            if(Objects.equals(mainView.getComTypeBox().getSelectedItem(), "Non reliable")) {
                createdGroup.setCommunicationMethod(new NonReliableMulticast());
            }
            if(Objects.equals(mainView.getOrderTypeBox().getSelectedItem(), "Unordered")){
                createdGroup.setOrderingMethod(new UnorderedMessageOrdering());
            }else if (mainView.getOrderTypeBox().getSelectedItem().equals("Causal")){
                createdGroup.setOrderingMethod(new CausalMessageOrdering());
            }

//            List<String> grouplist = new ArrayList<>();
            try {
                groupManager.createGroup(groupName,createdGroup);
                System.out.println("Create Group: "+ groupName+", leader: "+user.getId());
//                grouplist = nameStub.getAllGroups();
            } catch (RemoteException remoteException) {
                System.out.println("Failed to create group.");
                remoteException.printStackTrace();
            }
            updateGrouplist();
//            try {
//                updategrouplist();
//                System.out.println("Group list was updated.");
//            } catch (RemoteException remoteException) {
//                System.out.println("Failed to update group list.");
//            }
//            DefaultListModel<String> grouplistmodel = new DefaultListModel<>();
//            for (String g: grouplist){
//                grouplistmodel.addElement(g);
//            }
//            mainView.getGrouplistField().setModel(grouplistmodel);
        });
    }

    private void updateGrouplist() {
        List<String> groupList = new ArrayList<>();
        try {
            groupList = nameStub.getAllGroups();
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
            this.user = new User(username);
            try {
                this.registry = LocateRegistry.getRegistry(8888);
                this.nameStub = (INamingService) registry.lookup("NamingService");
                this.groupManager = new GroupManager(this.user,this.nameStub);
            } catch (RemoteException | NotBoundException remoteException) {
                remoteException.printStackTrace();
            }

            try {
                gComService = new GComService(this.user,this.groupManager);
                System.out.println("Init user: username - "+username);
                this.registry.rebind(username, (Remote) gComService);
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
