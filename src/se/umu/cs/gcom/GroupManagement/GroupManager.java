package se.umu.cs.gcom.GroupManagement;

import se.umu.cs.gcom.Naming.INamingService;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroupManager implements IGroupManagement, Serializable {
    private static final long serialVersionUID = -8747121065935799448L;
    private String groupId;
    private List<User> memberlist;
    private User currentUser;
    private User leader;
    private INamingService namestub;


    public GroupManager(User currentUser, INamingService nameService) {
        this.currentUser = currentUser;
        this.namestub = nameService;
        this.memberlist = new ArrayList<User>();

        memberlist.add(currentUser);
    }


    @Override
    public void joinGroup(String groupId) throws RemoteException {
        boolean leaderFlag = true;
        User leaderNaming = namestub.getLeader(groupId);
        //What happens if the leader server does not exist?
        try {
            leaderNaming.getgcomstub(groupId);
        } catch (NotBoundException e) {
            leaderFlag = false;
        }
        //update leader and naming server.
        if (!leaderFlag){
            leader = currentUser;
            namestub.updateGroup(groupId,leader);
        }else {
            leader = leaderNaming;
        }
        // update member list for all members in group.
        for (User m:memberlist){
            try {
                IGComService mStub = m.getgcomstub(groupId);
                GroupManager mGroup = mStub.getGroupManager();
                User mUser = mStub.getUser();

                mGroup.addMember(currentUser);
                this.addMember(mUser);
            } catch (NotBoundException ignored) {
            }
        }
        // notify member change.
        // register GCom.
    }

    @Override
    public void leaveGroup() throws RemoteException {
        boolean leaderFlag = false;
        if (currentUser.equals(leader)){
            leaderFlag = true;
        }

    }

    @Override
    public void createGroup(String groupId) throws RemoteException {
        namestub.createGroup(groupId,currentUser);
    }

    @Override
    public void removeGroup(String groupId) throws RemoteException {
        //1. remove group in naming
        //2. update member list? Might not be necessary at all. The group does not exist anymore.
        //It depends on the design of Group manager.
        namestub.deleteGroup(groupId);
    }


    @Override
    public void addMember(User member) throws RemoteException {
        if(!currentUser.getId().equals(member.getId())){
            memberlist.add(member);
        }
    }

    @Override
    public void removeMember(User member) throws RemoteException {
        memberlist.remove(member);
    }

    @Override
    public void notifyMemberJoined(User member) throws RemoteException {

    }

    @Override
    public void notifyMemberLeft(User member) throws RemoteException {

    }

    @Override
    public List<User> getAllMembers(String groupId) throws RemoteException {
        return this.memberlist;
    }

    @Override
    public User getLeader(String groupId) throws RemoteException {
        return this.leader;
    }


    @Override
    public List<String> getAllGroups() throws RemoteException {
        return namestub.getAllGroups();
    }

    @Override
    public HashMap<String, User> getUserMap() throws RemoteException {
        return namestub.getUserMap();
    }
}
