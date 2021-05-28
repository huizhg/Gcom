package se.umu.cs.gcom.GroupManagement;

import se.umu.cs.gcom.MessageOrdering.Message;

import java.rmi.RemoteException;

public interface IGComService {
    User getUser() throws RemoteException;
    GroupManager getGroupManager() throws RemoteException;

    void sendMessage(Message msg) throws RemoteException;
}
