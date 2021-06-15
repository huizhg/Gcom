package se.umu.cs.gcom.MessageOrdering;

import se.umu.cs.gcom.GroupManagement.User;

import javax.swing.*;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.PriorityBlockingQueue;

public class CausalMessageOrdering implements Ordering, Serializable {
    private static final long serialVersionUID = 6080610752003408072L;
    private PriorityBlockingQueue<Message> deliverQueue;
    private LinkedBlockingDeque<Message> messagesQueue;
    private VectorClock currentProcessClock;
    private VectorClock deliverClock;

    public CausalMessageOrdering(){
        messagesQueue = new LinkedBlockingDeque<>(11);
        deliverQueue = new PriorityBlockingQueue<>(11, new MessageComparator());
        currentProcessClock = new VectorClock();
        deliverClock = new VectorClock();
    }

    private boolean checkIncomingMsg(Message message,User user){
        UUID ownId = user.getUserID();
        UUID incomingId = message.getSender().getUserID();
//        System.out.println("ownId = "+ownId);
//        System.out.println("incomingId = "+incomingId);
        VectorClock inComingClock = message.getVectorClock();
//        Boolean outputFlag = false;
        if (currentProcessClock.getClock().keySet().isEmpty()){
            currentProcessClock.initialize(ownId);
            currentProcessClock.initialize(incomingId);
        }
        ArrayList<UUID> knownUsers = new ArrayList<>(currentProcessClock.getClock().keySet());
        knownUsers.addAll(inComingClock.getClock().keySet());
        for (UUID userID : knownUsers){
            if (deliverClock.getUserClock(userID)==null){
                deliverClock.initialize(userID);
            }
            if (inComingClock.getUserClock(userID)==null){
                inComingClock.initialize(userID);
            }
            if (currentProcessClock.getUserClock(userID)==null){
                currentProcessClock.initialize(userID);
            }
        }
//        System.out.println("Deliver = "+deliverClock.toString());
//        System.out.println("Incoming = "+inComingClock.toString());
//        System.out.println("current = "+currentProcessClock.toString());
//        System.out.println("In = own?"+incomingId.equals(ownId));
//        System.out.println("+1?"+inComingClock.getUserClock(ownId).equals(deliverClock.getUserClock(ownId)+1));
        if (incomingId.equals(ownId)) {
            if (inComingClock.getUserClock(ownId).equals(deliverClock.getUserClock(ownId) + 1)) {
                deliverClock.increment(user);
                return true;
            }else {
                return false;
            }
        } else if (inComingClock.getUserClock(incomingId) == currentProcessClock.getUserClock(incomingId)+1) {
            knownUsers.remove(incomingId);
            for (UUID userID : knownUsers) {
                if (inComingClock.getUserClock(userID) <= currentProcessClock.getUserClock(userID)) {
                    currentProcessClock.increment(message.getSender());
                    return true;
                }
            }
        }

        return false;
//        return false;
    }

    @Override
    public void receive(Message message, User user) {
        if (message.getMessageType()==MessageType.NORMAL) {
//        System.out.println("Received by ORdering.");
//        System.out.println("IncomingClock = "+message.getVectorClock().toString());
//        VectorClock inComingClock = message.getVectorClock();
////        // Increase the vector clock of the process which receive the message.
//////        System.out.println("Before Increment.");
////        currentProcessClock.increment(message.getSender());
//////        System.out.println("CurrentClock = "+currentProcessClock.toString());
////        // Compare the current process's vector clock with the clock come with a message
//        Integer incomingValue = inComingClock.getUserClock(message.getSender().getUserID());
//        System.out.println("IncomingClock = "+message.getVectorClock().toString());
//        Integer currentValue = currentProcessClock.getUserClock(user.getUserID());
//        System.out.println("CurrentClock = "+currentProcessClock.toString());
            Message incomingMsg = message;
            deliverQueue.put(message);
//        Boolean checkFlag = checkIncomingMsg(message, user);
//        System.out.println("Flag?"+checkFlag);
            while (checkIncomingMsg(incomingMsg, user)) {
                VectorClock inComingClock = incomingMsg.getVectorClock();
                currentProcessClock.update(inComingClock);
//            System.out.println("Update is done.");
//            System.out.println("Deliver queue size = "+deliverQueue.size());
                try {
                    messagesQueue.put(incomingMsg);
                    deliverQueue.remove(incomingMsg);
                    incomingMsg = deliverQueue.peek();
//                System.out.println("Put?"+messagesQueue.size());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//            currentProcessClock.increment(user);
                if (incomingMsg == null) {
//                if (deliverQueue.isEmpty()){
//                    deliverClock.update(currentProcessClock);
//                }
                    break;
                }
            }
//        while (incomingValue == currentValue + 1){

//        }

//        try {
//            VectorClock newClock = currentProcessClock.copy();
//            message.setVectorClock(newClock);
//        } catch (CloneNotSupportedException e) {
//            e.printStackTrace();
//        }


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
        else {
            try {
                messagesQueue.put(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public Message deliver() throws InterruptedException {
        return messagesQueue.take();
    }

    @Override
    public Message createMsg(Message message) {
//        System.out.println("Vector Clock!");
//        VectorClock oldclock = null;
//        try {
//            oldclock = currentProcessClock.copy();
//            oldclock.increment(message.getSender());
//        } catch (CloneNotSupportedException e) {
//            e.printStackTrace();
//        }
//        message.setVectorClock(oldclock);

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
