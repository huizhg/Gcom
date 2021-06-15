package se.umu.cs.gcom.GroupManagement;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;

public interface IGroupManagement {
    //1
    void joinGroup(String groupId) throws RemoteException;
    void leaveGroup() throws RemoteException;

    void createGroup(String groupId, Group group) throws RemoteException;
//    void createGroup(String groupId) throws RemoteException;
    void removeGroup(String groupId) throws RemoteException;

    void addMember(String memberName) throws RemoteException;
    void removeMember(String memberName) throws RemoteException;
    //3
    void notifyMemberJoined(User member);
    void notifyMemberLeft(User member);
    //4
    List<String> getAllMembers() throws RemoteException;
//    List<String> getAllMembers(String groupId) throws RemoteException;
    //NamingService
    User getLeader(String groupId) throws RemoteException;
    List<String> getAllGroups() throws RemoteException;
    HashMap<String, User> getUserMap() throws RemoteException;

}
