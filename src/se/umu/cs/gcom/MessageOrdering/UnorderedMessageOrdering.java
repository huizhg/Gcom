package se.umu.cs.gcom.MessageOrdering;

public class UnorderedMessageOrdering implements Ordering {
    @Override
    public void receive(Message message) {

    }

    @Override
    public Message deliver() {
        return null;
    }
}
