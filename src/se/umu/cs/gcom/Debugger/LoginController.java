package se.umu.cs.gcom.Debugger;


import se.umu.cs.gcom.GroupManagement.GComService;
import se.umu.cs.gcom.GroupManagement.GroupManager;
import se.umu.cs.gcom.GroupManagement.IGComService;
import se.umu.cs.gcom.GroupManagement.User;
import se.umu.cs.gcom.Naming.INamingService;
import se.umu.cs.gcom.TestClient.GComServer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class LoginController {
    private LoginView loginView;
    private User user;
    private GroupManager groupManager;
    private Registry registry;
    private INamingService nameStub;

    public LoginController(LoginView loginView) {
        this.loginView = loginView;
        init();
    }
    private void init(){
        loginView.getLoginButton().addActionListener(e -> {
            String username = loginView.getUserText().getText();
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
        });
    }
}
