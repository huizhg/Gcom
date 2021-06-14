package se.umu.cs.gcom.MessageOrdering;

import se.umu.cs.gcom.GroupManagement.User;

import javax.swing.*;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.PriorityBlockingQueue;

public class CausalMessageOrdering implements Ordering, Serializable {
    private static final long serialVersionUID = 6080610752003408072L;
    private PriorityBlockingQueue<Message> deliverQueue;
//    private LinkedBlockingDeque<Message> messagesQueue;
    private VectorClock currentProcessClock;

    public CausalMessageOrdering(){
//        messagesQueue = new LinkedBlockingDeque<>(11);
        deliverQueue = new PriorityBlockingQueue<>(11, new MessageComparator());
        currentProcessClock = new VectorClock();
    }



    @Override
    public void receive(Message message, User user) {
//        System.out.println("Received by ORdering.");
//        System.out.println("IncomingClock = "+message.getVectorClock().toString());
        VectorClock inComingClock = message.getVectorClock();
//        // Increase the vector clock of the process which receive the message.
////        System.out.println("Before Increment.");
        currentProcessClock.increment(message.getSender());
////        System.out.println("CurrentClock = "+currentProcessClock.toString());
//        // Compare the current process's vector clock with the clock come with a message
        currentProcessClock.update(inComingClock);
        try {
//            System.out.println("In the try?");
            VectorClock newClock = currentProcessClock.copy();
            message.setVectorClock(newClock);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        System.out.println("Causal Recieved.");

        deliverQueue.put(message);
//        ArrayList<Message> msglist = new ArrayList<>();
//        System.out.println("Queue size = "+deliverQueue.size());

//        Object[] msgArray = deliverQueue.toArray();
//        for (Object m: msgArray){
//            System.out.println(m.toString());
//        }
//        deliverQueue.drainTo(msglist);
//        System.out.println("Start to print.");
//        for (Message m:msglist){
//            System.out.println(m.toString());
//        }
//        System.out.println("Queue size = "+deliverQueue.size());

    }

    @Override
    public Message deliver() throws InterruptedException {
        return deliverQueue.take();
    }

    @Override
    public Message createMsg(Message message) {
//        System.out.println("Vector Clock!");
        VectorClock oldclock = null;
        try {
            oldclock = currentProcessClock.copy();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        message.setVectorClock(oldclock);

//        VectorClock inComingClock = message.getVectorClock();
        // Increase the vector clock of the process which receive the message.
//        System.out.println("Before Increment.");
        currentProcessClock.increment(message.getSender());
////        System.out.println("CurrentClock = "+currentProcessClock.toString());
//        // Compare the current process's vector clock with the clock come with a message
//        currentProcessClock.update(inComingClock);
        try {
//            System.out.println("In the try?");
            VectorClock newClock = currentProcessClock.copy();
            message.setVectorClock(newClock);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return message;
    }

    @Override
    public DefaultListModel<String> getqueuelistModel() {
        DefaultListModel<String> queuelistModel = new DefaultListModel<>();
        List<Message> messageList = new ArrayList<>();
        PriorityBlockingQueue<Message> queueCopy = new PriorityBlockingQueue<>(deliverQueue);
        while (!queueCopy.isEmpty()){
            try {
                messageList.add(queueCopy.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (Message m:messageList){
            queuelistModel.addElement(m.toString());
        }
        return queuelistModel;
    }


    private class MessageComparator implements Comparator<Message>,Serializable {

        @Override
        public int compare(Message message1, Message message2) {
            VectorClock v1 = message1.getVectorClock();
            VectorClock v2 = message2.getVectorClock();
            return v1.compareTo(v2);
        }
    }
}
