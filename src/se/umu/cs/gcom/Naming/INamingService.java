package se.umu.cs.gcom.Naming;

import se.umu.cs.gcom.GroupManagement.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;

public interface INamingService extends Remote {
    // HashMap<String, User> UserMap, used to store users with groupId.
    void createGroup(String groupId, User leader) throws RemoteException;
    void updateGroup(String groupId, User leader) throws RemoteException;
    void deleteGroup(String groupId) throws RemoteException;

    User getLeader(String groupId) throws RemoteException;
    List<String> getAllGroups() throws RemoteException;
    HashMap<String, User> getUserMap() throws RemoteException;
}
