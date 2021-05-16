package se.umu.cs.gcom.GroupManagement;

import java.rmi.RemoteException;

public interface IUser {
    String getId() throws RemoteException;

    void sendMessage(tempClassMsg msg) throws RemoteException;
}
