package se.umu.cs.gcom.GroupManagement;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class GComService extends UnicastRemoteObject implements IGComService {

    protected GComService(int port) throws RemoteException {
        super(port);
    }

    @Override
    public User getUser() throws RemoteException {
        return null;
    }

    @Override
    public GroupManager getGroupManager() throws RemoteException {
        return null;
    }

    @Override
    public void sendMessage(tempClassMsg msg) throws RemoteException {

    }
}
