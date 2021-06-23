package se.umu.cs.gcom.MessageOrdering;

import se.umu.cs.gcom.GCom.Message;
import se.umu.cs.gcom.GCom.User;

import javax.swing.*;
import java.io.Serializable;
import java.util.concurrent.LinkedBlockingDeque;

public class UnorderedMessageOrdering implements Ordering, Serializable {
    private static final long serialVersionUID = -1374620059951208354L;
    private final LinkedBlockingDeque<Message> messagesQueue;
    public UnorderedMessageOrdering(){
        messagesQueue = new LinkedBlockingDeque<>(11);
    }


    @Override
    public void receive(Message message, User user) throws InterruptedException {
        message.updateMsgPath("-Unordered");
        messagesQueue.put(message);

    }

    @Override
    public Message deliver() throws InterruptedException {
        return messagesQueue.take();

    }

    @Override
    public Message prepareMsg(Message message)
    {
        return message;
    }

    @Override
    public DefaultListModel<String> getqueuelistModel() {
        DefaultListModel<String> queuelistModel = new DefaultListModel<>();
        if (!messagesQueue.isEmpty()){
            Object[] msgArray = messagesQueue.toArray();
            for (Object m: msgArray){
                queuelistModel.addElement(m.toString());
            }
        }
        return queuelistModel;
    }
}
