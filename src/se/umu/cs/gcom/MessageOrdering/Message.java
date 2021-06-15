package se.umu.cs.gcom.MessageOrdering;

import se.umu.cs.gcom.GroupManagement.User;

import java.io.Serializable;

public class Message implements Serializable {
    private User sender;
    private MessageType messageType;
    private String messageContent;
    private VectorClock vectorClock;


    public Message(User sender, MessageType messageType) {
        this.sender = sender;
        this.messageType = messageType;
    }

    public Message(User sender, MessageType messageType, String messageContent) {
        this.sender = sender;
        this.messageType = messageType;
        this.messageContent = messageContent;
    }

    public void setVectorClock(VectorClock vectorClock){
        this.vectorClock = vectorClock;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public User getSender() {
        return sender;
    }

    public VectorClock getVectorClock() {
        return vectorClock;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    @Override
    public String toString() {
        if (vectorClock != null){
            return vectorClock.toString()+"-"+sender.getId()+":"+messageContent;
        }else {
            return sender.getId()+":"+messageContent;
        }
    }
}