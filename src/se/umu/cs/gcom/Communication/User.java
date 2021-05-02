package se.umu.cs.gcom.Communication;

import se.umu.cs.gcom.MessageOrdering.Message;

import java.util.UUID;

public class User {
    private  UUID userID;
    public User(){
        this.userID = UUID.randomUUID();
    }

    public UUID getUserID() {
        return userID;
    }
    public void sendMessage(Message message){
        return;
    }
}
