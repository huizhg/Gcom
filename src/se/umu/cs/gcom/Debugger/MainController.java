package se.umu.cs.gcom.Debugger;


import se.umu.cs.gcom.GroupManagement.GroupManager;
import se.umu.cs.gcom.GroupManagement.User;
import se.umu.cs.gcom.Naming.INamingService;

import java.rmi.registry.Registry;

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
//            this.user = new User(username);
//            try {
//                this.registry = LocateRegistry.getRegistry(8888);
//                this.nameStub = (INamingService) registry.lookup("NamingService");
//            } catch (RemoteException | NotBoundException remoteException) {
//                remoteException.printStackTrace();
//            }
//            this.groupManager = new GroupManager(this.user,this.nameStub);
//
//            try {
//                IGComService gComService = new GComService(this.user,this.groupManager);
//                System.out.println("Init user: username - "+username);
//                this.registry.rebind(username, (Remote) gComService);
//            } catch (RemoteException  remoteException) {
//                remoteException.printStackTrace();
//            }
            mainView.getLoginPanel().setEnabled(false);
            mainView.getLoginPanel().setVisible(false);
            mainView.buildUserView(username);
        });
    }
}
