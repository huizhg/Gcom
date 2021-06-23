package se.umu.cs.gcom.GCom;

import se.umu.cs.gcom.Communication.Communication;
import se.umu.cs.gcom.GroupManagement.GroupManager;
import se.umu.cs.gcom.MessageOrdering.Ordering;
import se.umu.cs.gcom.Naming.INamingService;

import javax.swing.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface IGComService extends Remote {
    User getUser() throws RemoteException;
    GroupManager getGroupManager() throws RemoteException;
    INamingService getNameStub() throws RemoteException;
    void updateMemberlist(List<String> mList) throws RemoteException;

    void sendMessage(Message msg) throws RemoteException;

    // Ordering
    Message deliver() throws RemoteException;
    Message prepareMsg(MessageType messageType,String msgContent) throws RemoteException;
    DefaultListModel<String> getqueuelistModel() throws RemoteException;
    // Communication
    List<String> multicast(Message msg) throws RemoteException;

    // Group Management
    //1
    void joinGroup(String groupId,String leaderId) throws RemoteException;

    void leaveGroup() throws RemoteException;
    void createGroup(String groupId, String comType, String orderType) throws RemoteException;
    //    void createGroup(String groupId) throws RemoteException;
    void removeGroup(String groupId) throws RemoteException;

    void addMember(String memberName) throws RemoteException;
    void removeMember(String memberName) throws RemoteException;
    //3
    void notifyMemberJoined(String memberName)throws RemoteException;
    void notifyMemberLeft(String memberName)throws RemoteException;
    //4
    List<String> getAllMembers()throws RemoteException;
    // My
    Map<Integer, List<String>> liveCheck () throws RemoteException;
    boolean checkNameStub() throws RemoteException;
    //NamingService
    User getLeader() throws RemoteException;
    List<String> getAllGroups() throws RemoteException;
    HashMap<String, User> getUserMap() throws RemoteException;
}
