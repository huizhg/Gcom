package se.umu.cs.gcom.MessageOrdering;

import se.umu.cs.gcom.GroupManagement.User;

import javax.swing.*;
import java.io.Serializable;
import java.util.concurrent.LinkedBlockingDeque;

public class UnorderedMessageOrdering implements Ordering, Serializable {
    private static final long serialVersionUID = -1374620059951208354L;
    private LinkedBlockingDeque<Message> messagesQueue;
    public UnorderedMessageOrdering(){
        messagesQueue = new LinkedBlockingDeque<>(11);
    }


    @Override
    public void receive(Message message, User user) throws InterruptedException {
//        System.out.println("Ordering Receive.");
        messagesQueue.put(message);
//        System.out.println("Receive size = "+messagesQueue.size());
//        Object[] msgArray = messagesQueue.toArray();
//        for (Object m: msgArray){
//            System.out.println(m.toString());
//        }
    }

    @Override
    public Message deliver() throws InterruptedException {
//        System.out.println("Ordering Deliver");
//        System.out.println("Deliver Size :"+messagesQueue.size());
        return messagesQueue.take();

    }

    @Override
    public Message createMsg (Message message)
    {
//        System.out.println("A normal message without vector clock.");
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
