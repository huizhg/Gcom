package se.umu.cs.gcom.Naming;

import se.umu.cs.gcom.GroupManagement.User;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NamingService extends UnicastRemoteObject implements INamingService{
    private static final long serialVersionUID = 4722301864571676308L;
    HashMap<String, User> userMap;

    public NamingService() throws RemoteException {
        userMap = new HashMap<>();
        System.out.println("Naming Service is started.");
    }

    @Override
    public void createGroup(String groupId, User leader) throws RemoteException {
        userMap.put(groupId, leader);
        System.out.println("Group Created: Id-"+groupId+" Leader-"+leader.getId());
    }

    @Override
    public void updateGroup(String groupId, User leader) throws RemoteException {
        userMap.remove(groupId);
        userMap.put(groupId, leader);
        System.out.println("Group Updated: Id-"+groupId+" Leader-"+leader.getId());
    }

    @Override
    public void deleteGroup(String groupId) throws RemoteException {
        userMap.remove(groupId);
        System.out.println("Group Deleted: Id-"+groupId);
    }

    @Override
    public User getLeader(String groupId) throws RemoteException {
        User leader = userMap.get(groupId);
        System.out.println("Access Group leader:"+leader.getId());
        return leader;
    }

    @Override
    public List<String> getAllGroups() throws RemoteException {
        List<String> grouplist = new ArrayList<String>(userMap.keySet());
        System.out.println("Access All Groups.");
        return grouplist;
    }

    @Override
    public HashMap<String, User> getUserMap() throws RemoteException {
        System.out.println("Access User Hash Map.");
        return userMap;
    }
}
