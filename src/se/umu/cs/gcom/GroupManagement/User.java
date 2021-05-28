package se.umu.cs.gcom.GroupManagement;

import se.umu.cs.gcom.MessageOrdering.Message;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class User implements IUser, Serializable {
    private static final long serialVersionUID = -3972163092132587040L;
    private String userId;
    private IGComService gcomstub;

    public User(String userId) {
        this.userId = userId;
    }

    public IGComService getgcomstub(String groupId) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(8888);
        // Different groups with same user need different gcomstub
        IGComService gcomstub = (IGComService) registry.lookup("GCom-"+groupId+"User-"+this.userId);
        return gcomstub;
    }

    @Override
    public String getId() throws RemoteException {
        return userId;
    }

    @Override
    public void sendMessage(Message msg) throws RemoteException {

    }
}
