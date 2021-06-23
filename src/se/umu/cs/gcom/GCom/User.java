package se.umu.cs.gcom.GCom;

import javax.swing.text.html.HTMLDocument;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class User implements Serializable {
    private static final long serialVersionUID = -3972163092132587040L;
    private String userId;
    private IGComService gcomstub;

    private final UUID userID;
    private final List<Group> groupList;

    public User(String userId) {
        this.userId = userId;
        this.userID = UUID.randomUUID();
        this.groupList = new ArrayList<>();
    }

    public User() {
        this.userID = UUID.randomUUID();
        this.groupList = new ArrayList<>();
    }

    public IGComService getgcomstub() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(8888);
        return (IGComService) registry.lookup(this.userId);
    }

    public UUID getUserID() {
        return userID;
    }

    public List<Group> getGroupList() {
        return groupList;
    }

    public Group getGroup(String groupName){
        for (Group g:groupList){
            if (g.getGroupName().equals(groupName)){
                return g;
            }
        }
        return null;
    }

    public void addGroup(Group newGroup){
        boolean flag = true;
        for (Group g:groupList){
            if (g.getGroupName().equals(newGroup.getGroupName())) {
                flag = false;
                break;
            }
        }
        if(flag){
            this.groupList.add(newGroup);
        }
    }

    public void removeGroup(String groupName){
        groupList.removeIf(group -> group.getGroupName().equals(groupName));
    }
    
    public String getId(){
        return userId;
    }

}
