package se.umu.cs.gcom.Client;

import se.umu.cs.gcom.Naming.INamingService;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class GroupUpdate extends SwingWorker<Void, List<String>> {
    private INamingService nameStub;
    private JList grouplistField;

    public GroupUpdate(INamingService nameStub, JList grouplistField) {
        this.nameStub = nameStub;
        this.grouplistField = grouplistField;
    }

    @Override
    protected Void doInBackground() throws Exception {
        while(!isCancelled()){
            List<String> groupList = new ArrayList<>();
            try {
                groupList = nameStub.getAllGroups();
//                System.out.println("Group list was updated.");
            } catch (RemoteException e) {
//                System.out.println("Failed to update group list.");
            }
            publish(groupList);

            Thread.sleep(5000);
        }
        return null;
    }

    @Override
    protected void process(List<List<String>> chunks) {
        if (chunks != null && !chunks.isEmpty()){
            List<String> groupList = chunks.get(chunks.size()-1);
            DefaultListModel<String> groupListModel = new DefaultListModel<>();
            for (String g: groupList){
                groupListModel.addElement(g);
            }
            grouplistField.setModel(groupListModel);
//            System.out.println("Background update - Grouplist.");
        }

    }
}
