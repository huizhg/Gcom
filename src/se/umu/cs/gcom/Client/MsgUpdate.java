package se.umu.cs.gcom.Client;

import se.umu.cs.gcom.GroupManagement.GroupManager;
import se.umu.cs.gcom.GCom.Message;
import se.umu.cs.gcom.GCom.MessageType;

import javax.swing.*;
import java.util.List;

public class MsgUpdate extends SwingWorker<Void, Message> {
    private GroupManager groupManager;
    private JList msglist;
    private JList BackendArea;
    private JList PerformanceArea;

    public MsgUpdate(GroupManager groupManager, JList msglist, JList BackendArea,JList PerformanceArea) {
        this.groupManager = groupManager;
        this.msglist = msglist;
        this.BackendArea = BackendArea;
        this.PerformanceArea=PerformanceArea;
    }

    @Override
    protected Void doInBackground() throws Exception {

        while(!isCancelled()){
            Message msg = groupManager.getCurrentGroup().getOrderingModule().deliver();

            publish(msg);
//            Thread.sleep(1000);
        }
        return null;
    }

    @Override
    protected void process(List<Message> chunks) {

        for (Message m:chunks){
            m.updateMsgPath("-Delivered");
            DefaultListModel msgListModel = (DefaultListModel) msglist.getModel();
            DefaultListModel backendModel = (DefaultListModel) BackendArea.getModel();
            DefaultListModel pModel = (DefaultListModel) PerformanceArea.getModel();
            if (m.getMessageType().equals(MessageType.NORMAL)){
                msgListModel.addElement(m.toString());
                msglist.setModel(msgListModel);
                System.out.println("Msg updated.");
            }else {
                msgListModel.addElement(m.getMessageContent());
                System.out.println("Notification Updated.");
            }
            long time = (System.currentTimeMillis()-m.getCreatedtime());
            String tempS = m.getCreatedtime()+"-"+time;
            System.out.println(m.toString()+tempS);
            int sec = (int) (time/1000);
            int ms = (int) (time%1000);
            String tStr = sec+"."+ms+"s";
            backendModel.addElement(m.getMessageType()+"-"+m.getSender().getId()+"-"+m.getMsgPath()+"-Time-"+tStr);
            BackendArea.setModel(backendModel);
            pModel.addElement("Msg Num: "+m.getPerformance());
            PerformanceArea.setModel(pModel);

        }
    }
}
