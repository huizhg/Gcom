package se.umu.cs.gcom.MessageOrdering;

import se.umu.cs.gcom.GroupManagement.User;

import javax.swing.*;
import java.util.concurrent.LinkedBlockingDeque;

public interface Ordering {
    void receive(Message message, User user) throws InterruptedException;
    Message deliver() throws InterruptedException;
    Message createMsg(Message message);
    DefaultListModel<String> getqueuelistModel();
}
