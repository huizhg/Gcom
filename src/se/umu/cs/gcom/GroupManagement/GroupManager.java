package se.umu.cs.gcom.GroupManagement;

import se.umu.cs.gcom.Communication.Communication;
import se.umu.cs.gcom.MessageOrdering.Message;
import se.umu.cs.gcom.MessageOrdering.Ordering;
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
//            System.out.println("Start to print mlist");
//            for(String m:mList){
//                System.out.println(m);
//            }
//            System.out.println("Done for mList");
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
//                System.out.println("Start to update ---");
//                System.out.println("Start to print mlist");
//                for(String m1:mList){
//                    System.out.println(m1);
//                }
//                System.out.println("Done for mList");
                System.out.println("Member "+mStub.getUser().getId()+" was updated.");
            } catch (Exception ignored){}
        }
        //-------
//        List<Group> groupList = new ArrayList<>();
//        try {
//            groupList = leader.getgcomstub().getUser().getGroupList();
//        } catch (NotBoundException e) {
//            e.printStackTrace();
//        }
//        System.out.println("User's group size = "+groupList.size());
//        for (Group g:groupList){
//            if (g.getGroupName().equals(groupId)){
//                currentGroup = g;
//                currentUser.addGroup(currentGroup);
//                break;
//            }
//        }
        try {
            currentGroup = leader.getgcomstub().getUser().getGroup(groupId);
            currentUser.addGroup(currentGroup);
        } catch (NotBoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void leaveGroup() throws RemoteException {
        boolean leaderFlag = false;
        if (currentUser.equals(leader)){
            leaderFlag = true;
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
    public void addMember(User member) throws RemoteException {
//        if(!currentUser.getId().equals(member.getId())){
//            memberlist.add(member);
//        }
    }

    @Override
    public void removeMember(User member) throws RemoteException {
//        memberlist.remove(member);
    }

    @Override
    public void notifyMemberJoined(User member) throws RemoteException {

    }

    @Override
    public void notifyMemberLeft(User member) throws RemoteException {

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
//        for (String l:liveM){
//            System.out.println("Live - "+liveM);
//        }
        setMemberlist(liveM);
        Map<Integer,List<String>> map= new HashMap<>();
        map.put(1,liveM);
        map.put(0,DeadM);
        return map;
    }
}
