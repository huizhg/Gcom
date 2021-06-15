package se.umu.cs.gcom.GroupManagement;

import se.umu.cs.gcom.Communication.Communication;
import se.umu.cs.gcom.MessageOrdering.Message;
import se.umu.cs.gcom.MessageOrdering.MessageType;
import se.umu.cs.gcom.MessageOrdering.Ordering;
import se.umu.cs.gcom.Naming.INamingService;

import javax.xml.bind.SchemaOutputResolver;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

public class GroupManager implements IGroupManagement, Serializable {
    private static final long serialVersionUID = -8747121065935799448L;
    private String groupId;
    private List<String> memberlist;
    private User currentUser;
    private User leader;
    private INamingService namestub;
    private Group currentGroup;

    public GroupManager(User currentUser, INamingService nameService) throws RemoteException {
        this.currentUser = currentUser;
        this.namestub = nameService;
        this.memberlist = new ArrayList<>();

        memberlist.add(currentUser.getId());
    }

    public Group getCurrentGroup() {
        return currentGroup;
    }
    public String getGroupId() {
        return groupId;
    }

    public void setMemberlist(List<String> memberlist) {
        this.memberlist = memberlist;
    }

    public List<String> multicast(Message msg){
//        System.out.println("Start to multicast in "+currentGroup.getGroupName());
        return currentGroup.getCommunicationMethod().multicast(memberlist,msg);
    }

    @Override
    public void joinGroup(String groupId) throws RemoteException {
        this.groupId = groupId;

        User leaderNaming = namestub.getLeader(groupId);
        leader = leaderNaming;
        System.out.println("Naming = "+leaderNaming.getId());
        System.out.println("Curr = "+currentUser.getId());

        boolean leaderFlag = leadercheck(leaderNaming);

        System.out.println("leaderFlag - "+leaderFlag);

        List<String> mList = new ArrayList<>();
        try {
            IGComService gcomStub = leader.getgcomstub();
            mList = gcomStub.getGroupManager().getAllMembers();
            if(!leaderFlag){
                mList.add(currentUser.getId());
            }

        } catch (NotBoundException e) {
            System.out.println("Registered Leader goes down.");
        }

        this.setMemberlist(mList);
        Map<Integer, List<String>> map = liveCheck();
        List<String> liveM = map.get(1);

        for (String m : liveM) {
            try {
                Registry registry = LocateRegistry.getRegistry(8888);
                IGComService mStub = (IGComService) registry.lookup(m);
                mStub.updateMemberlist(liveM);

                System.out.println("Member "+mStub.getUser().getId()+" was updated.");
            } catch (Exception ignored){}
        }

        try {
            currentGroup = leader.getgcomstub().getUser().getGroup(groupId);
            currentUser.addGroup(currentGroup);
        } catch (NotBoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void leaveGroup() throws RemoteException {
        if(memberlist.size()==1){
            System.out.println("Remove Group = "+groupId);
            removeGroup(groupId);
        }else {
            notifyMemberLeft(currentUser);
            removeMember(currentUser.getId());
            this.memberlist.clear();
            this.memberlist.add(currentUser.getId());
            if (leadercheck(leader)){
                System.out.println("True leader.");
                String newLeaderName = memberlist.get(0);
                Registry registry = LocateRegistry.getRegistry(8888);
                try {
                    IGComService mStub = (IGComService) registry.lookup(newLeaderName);
                    User newLeader = mStub.getUser();
                    namestub.updateGroup(groupId,newLeader);
                } catch (NotBoundException e) {
                    e.printStackTrace();
                }
            }else {
                System.out.println("False leader.");
            }

        }
    }

    @Override
    public void createGroup(String groupId, Group group) throws RemoteException {
        namestub.createGroup(groupId,currentUser);
        currentUser.addGroup(group);
    }

    @Override
    public void removeGroup(String groupId) throws RemoteException {
        namestub.deleteGroup(groupId);
    }


    @Override
    public void addMember(String memberName) throws RemoteException {
        Map<Integer, List<String>> map = liveCheck();
        List<String> liveM = map.get(1);
        liveM.add(memberName);
        for (String m : liveM) {
            try {
                Registry registry = LocateRegistry.getRegistry(8888);
                IGComService mStub = (IGComService) registry.lookup(m);
                mStub.updateMemberlist(liveM);
                System.out.println("Member "+mStub.getUser().getId()+" was updated.");
            } catch (Exception ignored){}
        }
    }

    @Override
    public void removeMember(String memberName) throws RemoteException {
        Map<Integer, List<String>> map = liveCheck();
        List<String> liveM = map.get(1);
        liveM.remove(memberName);
        for (String m : liveM) {
            try {
                Registry registry = LocateRegistry.getRegistry(8888);
                IGComService mStub = (IGComService) registry.lookup(m);
                mStub.updateMemberlist(liveM);
                System.out.println("Member "+mStub.getUser().getId()+" was updated.");
            } catch (Exception ignored){}
        }
    }

    @Override
    public void notifyMemberJoined(User member) {
        String content = "Notification: "+member.getId()+" joins Group - "+groupId;
        Message msg = new Message(member, MessageType.Notify,content);
//        System.out.println("Ready to send join notification.");
        multicast(msg);
    }

    @Override
    public void notifyMemberLeft(User member) {
        String content = "Notification: "+member.getId()+" leaves Group - " +groupId;
        Message msg = new Message(member, MessageType.Notify,content);
//        System.out.println("Ready to send leave notification.");
        multicast(msg);
    }

    @Override
    public List<String> getAllMembers() throws RemoteException {
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

    private boolean leadercheck(User leaderNaming) throws RemoteException {
        // leader check
        boolean leaderFlag;
        if (leaderNaming.getId().equals(currentUser.getId())){
            leaderFlag = true;
        }else {
            leaderFlag = false;
        }
        return leaderFlag;
    }
    public Map<Integer, List<String>> liveCheck () throws RemoteException{
        List<String> mList = new ArrayList<>();
        List<String> liveM = new ArrayList<>();
        List<String> DeadM = new ArrayList<>();
        try {
            mList = getAllMembers();
        } catch (RemoteException remoteException) {
            remoteException.printStackTrace();
        }

        for (String m:mList){
            Registry registry = null;
            try {
                registry = LocateRegistry.getRegistry(8888);
                IGComService mStub = (IGComService) registry.lookup(m);
                mStub.getUser();
                liveM.add(m);
            } catch (Exception e) {
                DeadM.add(m);
            }
        }
        setMemberlist(liveM);
        Map<Integer,List<String>> map= new HashMap<>();
        map.put(1,liveM);
        map.put(0,DeadM);
        return map;
    }
}
