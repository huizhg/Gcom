package se.umu.cs.gcom.Debugger;

import se.umu.cs.gcom.GroupManagement.GroupManager;
import se.umu.cs.gcom.MessageOrdering.Message;
import se.umu.cs.gcom.MessageOrdering.MessageType;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class MsgUpdate extends SwingWorker<Void, Message> {
    private GroupManager groupManager;
    private JList msglist;

    public MsgUpdate(GroupManager groupManager, JList msglist) {
        this.groupManager = groupManager;
        this.msglist = msglist;
    }

    @Override
    protected Void doInBackground() throws Exception {

        while(!isCancelled()){
            Message msg = groupManager.getCurrentGroup().getOrderingMethod().deliver();
            publish(msg);
//            Thread.sleep(1000);
        }
        return null;
    }



    @Override
    protected void process(List<Message> chunks) {

        for (Message m:chunks){
            DefaultListModel msgListModel = (DefaultListModel) msglist.getModel();
            if (m.getMessageType().equals(MessageType.NORMAL)){
                msgListModel.addElement(m.toString());
                msglist.setModel(msgListModel);
                System.out.println("Msg updated.");
            }else {
                msgListModel.addElement(m.getMessageContent());
                System.out.println("Notification Updated.");
            }
        }
    }
}
