package se.umu.cs.gcom.Communication;

import se.umu.cs.gcom.MessageOrdering.Message;
import se.umu.cs.gcom.MessageOrdering.Ordering;

public class NonReliableMulticast extends Communication{

    NonReliableMulticast(Ordering orderingMethod) {
        super(orderingMethod);
    }

    @Override
    public void receive(Message message) {

    }

    @Override
    public Message deliver() {
        return null;
    }
}
