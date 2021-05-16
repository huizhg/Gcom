package se.umu.cs.gcom.GroupManagement;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;

public class NamingService extends UnicastRemoteObject implements INamingService{
    protected NamingService() throws RemoteException {
    }

    @Override
    public void createGroup(String groupId, User leader) throws RemoteException {

    }

    @Override
    public void deleteGroup(String groupId) throws RemoteException {

    }

    @Override
    public User getLeader(String groupId) throws RemoteException {
        return null;
    }

    @Override
    public List<String> getAllGroups() throws RemoteException {
        return null;
    }

    @Override
    public HashMap<String, User> getUserMap() throws RemoteException {
        return null;
    }
}
