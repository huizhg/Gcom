package se.umu.cs.gcom.MessageOrdering;

import se.umu.cs.gcom.GroupManagement.User;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = -5012466034995408607L;
    private User sender;
    private MessageType messageType;
    private String messageContent;
    private VectorClock vectorClock;
    private String msgPath = "Created.";
    private long createdtime;
    private int performance;


    public Message(User sender, MessageType messageType) {
        this.sender = sender;
        this.messageType = messageType;
        this.createdtime = System.currentTimeMillis();
    }

    public Message(User sender, MessageType messageType, String messageContent) {
        this.sender = sender;
        this.messageType = messageType;
        this.messageContent = messageContent;
        this.createdtime = System.currentTimeMillis();
    }

    public int getPerformance() {
        return performance;
    }

    public void setPerformance(int performance) {
        this.performance = performance;
    }

    public long getCreatedtime() {
        return createdtime;
    }

    public String getMsgPath() {
        return msgPath;
    }

    public void updateMsgPath(String msgPath) {
        this.msgPath += msgPath;
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