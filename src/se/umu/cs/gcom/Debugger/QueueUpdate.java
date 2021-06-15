package se.umu.cs.gcom.Debugger;

import se.umu.cs.gcom.MessageOrdering.Ordering;

import javax.swing.*;
import java.util.List;

public class QueueUpdate extends SwingWorker<Void, DefaultListModel<String>> {

    private Ordering ordering;
    private JList debugqueueList;

    public QueueUpdate(Ordering ordering, JList debugqueueList) {
        this.ordering = ordering;
        this.debugqueueList = debugqueueList;
    }

    @Override
    protected Void doInBackground() throws Exception {
        while(!isCancelled()){
            DefaultListModel<String> queue = ordering.getqueuelistModel();
//            System.out.println("Queue size = "+queue.size());
            publish(queue);
            Thread.sleep(1000);
        }
        return null;
    }

    @Override
    protected void process(List<DefaultListModel<String>> chunks) {
        if (chunks != null && !chunks.isEmpty()){
//            System.out.println("Update Queue");
            DefaultListModel<String> queue = chunks.get(chunks.size()-1);
            debugqueueList.setModel(queue);
        }
    }
}
