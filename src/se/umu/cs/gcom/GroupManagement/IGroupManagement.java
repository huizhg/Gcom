package se.umu.cs.gcom.GroupManagement;

import se.umu.cs.gcom.GCom.Group;
import se.umu.cs.gcom.GCom.User;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IGroupManagement {
    //1
    void joinGroup(String groupId,String leaderId) throws RemoteException;
    void leaveGroup() throws RemoteException;

    void createGroup(String groupId, Group group) throws RemoteException;
    void removeGroup(String groupId) throws RemoteException;

    void addMember(String memberName) throws RemoteException;
    void removeMember(String memberName) throws RemoteException;
    //3
//    void notifyMemberJoined(User member);
//    void notifyMemberLeft(User member);
    //4
    List<String> getAllMembers();
    //NamingService
    User getLeader();
    List<String> getAllGroups() throws RemoteException;
    HashMap<String, User> getUserMap() throws RemoteException;

    //
    Map<Integer, List<String>> liveCheck ();
    boolean checkNameStub() throws RemoteException;
}
