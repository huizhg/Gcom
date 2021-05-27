package se.umu.cs.gcom.MessageOrdering;

public class CausalMessageOrdering implements Ordering {
    @Override
    public void receive(Message message) {

    }

    @Override
    public Message deliver() {
        return null;
    }
}
