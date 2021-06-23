package se.umu.cs.gcom.GCom;

import se.umu.cs.gcom.Communication.Communication;
import se.umu.cs.gcom.Communication.NonReliableMulticast;
import se.umu.cs.gcom.GroupManagement.GroupManager;
import se.umu.cs.gcom.MessageOrdering.CausalMessageOrdering;
import se.umu.cs.gcom.MessageOrdering.Ordering;
import se.umu.cs.gcom.MessageOrdering.UnorderedMessageOrdering;
import se.umu.cs.gcom.Naming.INamingService;

import javax.swing.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GComService extends UnicastRemoteObject implements IGComService {
    private static final long serialVersionUID = -2712648834546112309L;
    private User user;
    private GroupManager groupManager;
    private Ordering orderingModule;
    private Communication communicationModule;

    public GComService(String userName) throws RemoteException {
        this.user = new User(userName);
        Registry registry = LocateRegistry.getRegistry(8888);
        try {
            INamingService nameStub = (INamingService) registry.lookup("NamingService");
            this.groupManager = new GroupManager(user,nameStub);
            System.out.println("Naming Exists...");

        } catch (NotBoundException e) {
            this.groupManager = new GroupManager(user);
            System.out.println("Naming Fails...");
        }
        registry.rebind(userName,this);
    }

    public GComService(User user, GroupManager groupManager) throws RemoteException {
        this.user = user;
        this.groupManager = groupManager;
    }

    public INamingService getNameStub() throws RemoteException{
        return groupManager.getNamestub();
    }

    @Override
    public User getUser() throws RemoteException {
        return user;
    }

    @Override
    public GroupManager getGroupManager() throws RemoteException {
        return groupManager;
    }

    @Override
    public void updateMemberlist(List<String> mList) throws RemoteException {
        groupManager.setMemberlist(mList);
    }


    @Override
    public void sendMessage(Message msg) throws RemoteException{
        System.out.println("GCOM send message to ordering = "+msg.getMessageContent());
        msg.updateMsgPath("-Received");
        this.orderingModule = groupManager.getCurrentGroup().getOrderingModule();
        try {
            orderingModule.receive(msg,user);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Message deliver() throws RemoteException{
        this.orderingModule = groupManager.getCurrentGroup().getOrderingModule();
        try {
            return orderingModule.deliver();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Message prepareMsg(MessageType messageType,String msgContent) throws RemoteException{
        Message msg = new Message(user, messageType,msgContent);
        this.orderingModule = groupManager.getCurrentGroup().getOrderingModule();
        return orderingModule.prepareMsg(msg);
    }

    @Override
    public DefaultListModel<String> getqueuelistModel() throws RemoteException{
        this.orderingModule = groupManager.getCurrentGroup().getOrderingModule();
        return orderingModule.getqueuelistModel();
    }

    @Override
    public List<String> multicast(Message msg) throws RemoteException{
        List<String> mlist = groupManager.getAllMembers();
        this.communicationModule = groupManager.getCurrentGroup().getCommunicationModule();
        return communicationModule.multicast(mlist,msg);
    }

    @Override
    public void joinGroup(String groupId,String leaderId) throws RemoteException {
        groupManager.joinGroup(groupId,leaderId);
        notifyMemberJoined(user.getId());
    }

    @Override
    public void leaveGroup() throws RemoteException {
        notifyMemberLeft(user.getId());
        groupManager.leaveGroup();
    }

    @Override
    public void createGroup(String groupId, String comType, String orderType) throws RemoteException {
        Group group = new Group(groupId);
        if (comType.equals("Non reliable")){
            group.setCommunicationModule(new NonReliableMulticast());
            if (orderType.equals("Unordered")){
                group.setOrderingModule(new UnorderedMessageOrdering());
            }else if (orderType.equals("Causal")){
                group.setOrderingModule(new CausalMessageOrdering());
            }
        }
        groupManager.createGroup(groupId,group);
    }

    @Override
    public void removeGroup(String groupId) throws RemoteException {
        groupManager.removeGroup(groupId);
    }

    @Override
    public void addMember(String memberName) throws RemoteException {
        groupManager.addMember(memberName);
        notifyMemberJoined(memberName);
    }

    @Override
    public void removeMember(String memberName) throws RemoteException {
        groupManager.removeMember(memberName);
        notifyMemberLeft(memberName);
    }

    @Override
    public void notifyMemberJoined(String memberName) throws RemoteException {
        String content = "Notification: "+memberName+" joins Group - "+groupManager.getGroupId();
        Message msg = new Message(user, MessageType.Notification,content);
        multicast(msg);
    }

    @Override
    public void notifyMemberLeft(String memberName) throws RemoteException {
        String content = "Notification: "+memberName+" leaves Group - " +groupManager.getGroupId();
        Message msg = new Message(user, MessageType.Notification,content);
        multicast(msg);
    }

    @Override
    public List<String> getAllMembers() throws RemoteException {
        return groupManager.getAllMembers();
    }

    @Override
    public Map<Integer, List<String>> liveCheck() throws RemoteException {
        return groupManager.liveCheck();
    }

    @Override
    public boolean checkNameStub() throws RemoteException {
        return groupManager.checkNameStub();
    }

    @Override
    public User getLeader() throws RemoteException {
        return groupManager.getLeader();
    }

    @Override
    public List<String> getAllGroups() throws RemoteException {
        return groupManager.getAllGroups();
    }

    @Override
    public HashMap<String, User> getUserMap() throws RemoteException {
        return groupManager.getUserMap();
    }
}
