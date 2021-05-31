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
    private void groupController(){
        updategrouplist();
        mainView.getCreateButton().addActionListener(e -> {
            String groupname = mainView.getCreateGroupField().getText();
//            List<String> grouplist = new ArrayList<>();
            try {
                groupManager.createGroup(groupname);
                System.out.println("Create Group: "+ groupname+", leader: "+user.getId());
//                grouplist = nameStub.getAllGroups();
            } catch (RemoteException remoteException) {
                System.out.println("Failed to create group.");
            }
            updategrouplist();
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
    private void updategrouplist() {
        List<String> grouplist = new ArrayList<>();
        try {
            grouplist = nameStub.getAllGroups();
            System.out.println("Group list was updated.");
        } catch (RemoteException e) {
            System.out.println("Failed to update group list.");
        }
        DefaultListModel<String> grouplistmodel = new DefaultListModel<>();
        for (String g: grouplist){
            grouplistmodel.addElement(g);
        }
        mainView.getGrouplistField().setModel(grouplistmodel);
    }
}
