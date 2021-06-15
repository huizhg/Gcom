package se.umu.cs.gcom.Communication;

import se.umu.cs.gcom.GroupManagement.User;
import se.umu.cs.gcom.MessageOrdering.Message;
import se.umu.cs.gcom.MessageOrdering.Ordering;

import java.io.Serializable;
import java.util.List;

public abstract class Communication implements Serializable {
    private static final long serialVersionUID = -4864869307212215869L;

    Communication(){

    }
    abstract public List<String> multicast(List<String> memberlist, Message message);
//    abstract public void receive(Message message, User user) throws InterruptedException;
//    abstract public Message deliver() throws InterruptedException;
}
