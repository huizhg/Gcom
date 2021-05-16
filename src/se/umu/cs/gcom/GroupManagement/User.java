package se.umu.cs.gcom.GroupManagement;

import java.io.Serializable;
import java.rmi.RemoteException;

public class User implements IUser, Serializable {
    @Override
    public String getId() throws RemoteException {
        return null;
    }

    @Override
    public void sendMessage(tempClassMsg msg) throws RemoteException {

    }
}
