package se.umu.cs.gcom.Debugger;


import se.umu.cs.gcom.GroupManagement.GComService;
import se.umu.cs.gcom.GroupManagement.GroupManager;
import se.umu.cs.gcom.GroupManagement.IGComService;
import se.umu.cs.gcom.GroupManagement.User;
import se.umu.cs.gcom.Naming.INamingService;

import javax.swing.*;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class MainController {
    private MainView mainView;
    private User user;
    private GroupManager groupManager;
    private Registry registry;
    private INamingService nameStub;

    public MainController(MainView mainView) {
        this.mainView = mainView;
        loginController();
    }
    private void loginController(){
        loginButtonListener();
    }
    private void groupController(){
        updateGrouplist();
        createButtonListener();
        removeButtionListener();
        joinButtionListener();
    }
    private void userController(){

    }
    private void joinButtionListener(){
        mainView.getJoinButton().addActionListener(e -> {
            String groupName = mainView.getJoinGroupField().getText();
            String username = "";
            try {
//                groupManager.joinGroup(groupName);
                username = user.getId();
                System.out.println("Join Group: "+ groupName+", User: "+username);
            } catch (RemoteException remoteException) {
                System.out.println("Failed to join group.");
            }
            updateGrouplist();

            mainView.getGroupPanel().setEnabled(false);
            mainView.getGroupPanel().setVisible(false);
            mainView.buildUserView(username);
            userController();
        });
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
//            List<String> grouplist = new ArrayList<>();
            try {
                groupManager.createGroup(groupName);
                System.out.println("Create Group: "+ groupName+", leader: "+user.getId());
//                grouplist = nameStub.getAllGroups();
            } catch (RemoteException remoteException) {
                System.out.println("Failed to create group.");
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
        DefaultListModel<String> grouplistmodel = new DefaultListModel<>();
        for (String g: groupList){
            grouplistmodel.addElement(g);
        }
        mainView.getGrouplistField().setModel(grouplistmodel);
    }

    private void loginButtonListener(){
        mainView.getLoginButton().addActionListener(e -> {
            String username = mainView.getUserText().getText();
            this.user = new User(username);
            try {
                this.registry = LocateRegistry.getRegistry(8888);
                this.nameStub = (INamingService) registry.lookup("NamingService");
            } catch (RemoteException | NotBoundException remoteException) {
                remoteException.printStackTrace();
            }
            this.groupManager = new GroupManager(this.user,this.nameStub);

            try {
                IGComService gComService = new GComService(this.user,this.groupManager);
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
