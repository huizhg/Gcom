package se.umu.cs.gcom.GroupManagement;

import se.umu.cs.gcom.MessageOrdering.Message;
import se.umu.cs.gcom.MessageOrdering.Ordering;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class GComService extends UnicastRemoteObject implements IGComService {
    private static final long serialVersionUID = -2712648834546112309L;
    private User user;
    private GroupManager groupManager;
    private Ordering orderingMethod;

    public GComService(User user, GroupManager groupManager) throws RemoteException {
        this.user = user;
        this.groupManager = groupManager;
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
    public void sendMessage(Message msg) throws RemoteException {
        System.out.println("GCOM send message to ordering = "+msg.getMessageContent());
        this.orderingMethod = groupManager.getCurrentGroup().getOrderingMethod();
        try {
//            msg = orderingMethod.createMsg(msg);
            orderingMethod.receive(msg,user);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
