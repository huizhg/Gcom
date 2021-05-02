package se.umu.cs.gcom.MessageOrdering;

public interface Ordering {
    public void receive(Message message);
    public Message deliver();
}
