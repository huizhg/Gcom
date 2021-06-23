package se.umu.cs.gcom.MessageOrdering;

import se.umu.cs.gcom.GCom.Message;
import se.umu.cs.gcom.GCom.User;

import javax.swing.*;

public interface Ordering {
    void receive(Message message, User user) throws InterruptedException;
    Message deliver() throws InterruptedException;
    Message prepareMsg(Message message);
    DefaultListModel<String> getqueuelistModel();
}
