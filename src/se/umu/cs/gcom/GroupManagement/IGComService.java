package se.umu.cs.gcom.GroupManagement;

import se.umu.cs.gcom.MessageOrdering.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IGComService extends Remote {
    User getUser() throws RemoteException;
    GroupManager getGroupManager() throws RemoteException;
    void updateMemberlist(List<String> mList) throws RemoteException;

    void sendMessage(Message msg) throws RemoteException;
}
