package se.umu.cs.gcom.GroupManagement;

import se.umu.cs.gcom.MessageOrdering.Message;

import java.rmi.RemoteException;

public interface IUser {
//    String getId() throws RemoteException;
    String getId();
    void sendMessage(Message msg) throws RemoteException;
}
