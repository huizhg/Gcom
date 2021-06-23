package se.umu.cs.gcom.MessageOrdering;

import se.umu.cs.gcom.GCom.Message;
import se.umu.cs.gcom.GCom.MessageType;
import se.umu.cs.gcom.GCom.User;

import javax.swing.*;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.PriorityBlockingQueue;

public class CausalMessageOrdering implements Ordering, Serializable {
    private static final long serialVersionUID = 6080610752003408072L;
    private final PriorityBlockingQueue<Message> deliverQueue;
    private final LinkedBlockingDeque<Message> messagesQueue;
    private final VectorClock currentProcessClock;
    private final VectorClock deliverClock;

    public CausalMessageOrdering(){
        messagesQueue = new LinkedBlockingDeque<>(11);
        deliverQueue = new PriorityBlockingQueue<>(11, new MessageComparator());
        currentProcessClock = new VectorClock();
        deliverClock = new VectorClock();
    }

    private boolean checkIncomingMsg(Message message,User user){
        UUID ownId = user.getUserID();
        UUID incomingId = message.getSender().getUserID();
        VectorClock inComingClock = message.getVectorClock();
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
    }

    @Override
    public void receive(Message message, User user) {
        message.updateMsgPath("-Causal");
        if (message.getMessageType()== MessageType.NORMAL) {

            Message incomingMsg = message;
            deliverQueue.put(message);

            while (checkIncomingMsg(incomingMsg, user)) {
                VectorClock inComingClock = incomingMsg.getVectorClock();
                currentProcessClock.update(inComingClock);

                try {
                    messagesQueue.put(incomingMsg);
                    deliverQueue.remove(incomingMsg);
                    incomingMsg = deliverQueue.peek();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (incomingMsg == null) {
                    break;
                }
            }

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
    public Message prepareMsg(Message message) {

        message.updateMsgPath("-AddClock");
        currentProcessClock.increment(message.getSender());

        try {
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


    private static class MessageComparator implements Comparator<Message>,Serializable {
        private static final long serialVersionUID = -4365651154312787320L;

        @Override
        public int compare(Message message1, Message message2) {
            VectorClock v1 = message1.getVectorClock();
            VectorClock v2 = message2.getVectorClock();
            return v1.compareTo(v2);
        }
    }
}
