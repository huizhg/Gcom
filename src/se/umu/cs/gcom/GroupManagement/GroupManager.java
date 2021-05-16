package se.umu.cs.gcom.GroupManagement;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;

public class GroupManager implements IGroupManagement, Serializable {

    @Override
    public void joinGroup(String groupId) throws RemoteException {

    }

    @Override
    public void removeGroup(String groupId) throws RemoteException {

    }

    @Override
    public void addMember(User member) throws RemoteException {

    }

    @Override
    public void removeMember(User member) throws RemoteException {

    }

    @Override
    public void notifyMemberJoined(User member) throws RemoteException {

    }

    @Override
    public void notifyMemberLeft(User member) throws RemoteException {

    }

    @Override
    public List<User> getAllMembers(String groupId) throws RemoteException {
        return null;
    }

    @Override
    public void getLeader(String groupId) throws RemoteException {

    }


    @Override
    public List<String> getAllGroups() throws RemoteException {
        return null;
    }

    @Override
    public HashMap<String, User> getUserMap() throws RemoteException {
        return null;
    }
}
