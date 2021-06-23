package se.umu.cs.gcom.GroupManagement;

import se.umu.cs.gcom.GCom.*;
import se.umu.cs.gcom.Naming.INamingService;

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

    public GroupManager(User currentUser) throws RemoteException{
        this.currentUser = currentUser;
        this.memberlist = new ArrayList<>();
        memberlist.add(currentUser.getId());
    }

    public GroupManager(User currentUser, INamingService nameService) throws RemoteException {
        this.currentUser = currentUser;
        this.namestub = nameService;
        this.memberlist = new ArrayList<>();

        memberlist.add(currentUser.getId());
    }

    public INamingService getNamestub() {
        return namestub;
    }

    public String getGroupId() {
        return groupId;
    }

    public Group getCurrentGroup() {
        return currentGroup;
    }

    public void setMemberlist(List<String> memberlist) {
        this.memberlist = memberlist;
    }


    @Override
    public void joinGroup(String groupId,String leaderId) throws RemoteException {
        boolean leaderFlag = false;
        if (groupId!=null){
            System.out.println("WithNaming");
            this.groupId = groupId;
            User leaderNaming = namestub.getLeader(groupId);
            this.leader = leaderNaming;
            System.out.println("Naming = "+leaderNaming.getId());
            System.out.println("Curr = "+currentUser.getId());
            leaderFlag = leadercheck(leaderNaming);
        }else {
            System.out.println("WithoutNaming");
            Registry registry = LocateRegistry.getRegistry(8888);
            IGComService mStub;
            try {
                mStub = (IGComService) registry.lookup(leaderId);
                this.leader = mStub.getGroupManager().getLeader();
                this.groupId = mStub.getGroupManager().getGroupId();
                leaderFlag = leadercheck(leader);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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
            currentGroup = leader.getgcomstub().getUser().getGroup(this.groupId);
            currentUser.addGroup(currentGroup);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void leaveGroup() throws RemoteException {
        if(memberlist.size()==1){
            System.out.println("Remove Group = "+groupId);
            removeGroup(groupId);
        }else {
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
                } catch (NotBoundException ignored) {
                }
            }else {
                System.out.println("False leader.");
            }

        }
    }

    @Override
    public void createGroup(String groupId, Group group) throws RemoteException {
        try {
            namestub.createGroup(groupId,currentUser);
        }catch (Exception e){
            leader = currentUser;
            this.groupId = groupId;
            System.out.println("namestub down.");
        }
        currentUser.addGroup(group);
    }

    @Override
    public void removeGroup(String groupId) throws RemoteException {
        try {
            namestub.deleteGroup(groupId);
        }catch (Exception ignored){

        }
        currentUser.removeGroup(groupId);
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
    public List<String> getAllMembers()  {
        return this.memberlist;
    }

    @Override
    public User getLeader() {
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

    private boolean leadercheck(User leaderNaming) {
        // leader check
        boolean leaderFlag;
        leaderFlag = leaderNaming.getId().equals(currentUser.getId());
        return leaderFlag;
    }
    public Map<Integer, List<String>> liveCheck (){
        List<String> mList;
        List<String> liveM = new ArrayList<>();
        List<String> DeadM = new ArrayList<>();
        mList = getAllMembers();

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

    @Override
    public boolean checkNameStub() throws RemoteException {
        Registry registry = LocateRegistry.getRegistry(8888);
        try {
            INamingService nameStub = (INamingService) registry.lookup("NamingService");
            return true;
        } catch (NotBoundException e) {
            return false;
        }
    }
}
