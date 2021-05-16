package se.umu.cs.gcom.GroupManagement;

import java.rmi.RemoteException;

public interface IGComService {
    User getUser() throws RemoteException;
    GroupManager getGroupManager() throws RemoteException;

    void sendMessage(tempClassMsg msg) throws RemoteException;
}
