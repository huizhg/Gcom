package se.umu.cs.gcom.Debugger;

import se.umu.cs.gcom.GroupManagement.GroupManager;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MemberUpdate extends SwingWorker<Void, List<String>> {
    private GroupManager groupManager;
    private JList memberlist;

    public MemberUpdate(GroupManager groupManager, JList memberlist) {
        this.groupManager = groupManager;
        this.memberlist = memberlist;
    }

    @Override
    protected Void doInBackground() throws Exception {
        while(!isCancelled()){
            List<String> liveM = new ArrayList<>();
            try {
                Map<Integer, List<String>> map = groupManager.liveCheck();
                liveM = map.get(1);
                publish(liveM);
            } catch (RemoteException remoteException) {
                remoteException.printStackTrace();
            }

            Thread.sleep(5000);
        }
        return null;
    }

    @Override
    protected void process(List<List<String>> chunks) {
        if (chunks != null && !chunks.isEmpty()) {
            List<String> liveM = chunks.get(chunks.size()-1);
            DefaultListModel<String> mListModel = new DefaultListModel<>();
            for (String m:liveM){
                mListModel.addElement(m);
            }
            memberlist.setModel(mListModel);
        }

    }
}
