package se.umu.cs.gcom.Communication;

import se.umu.cs.gcom.MessageOrdering.Message;
import se.umu.cs.gcom.MessageOrdering.Ordering;

abstract class Communication {
    Ordering orderingMethod;
    Communication(Ordering orderingMethod){
        this.orderingMethod = orderingMethod;
    }
    abstract public void receive(Message message);
    abstract public Message deliver();
}
