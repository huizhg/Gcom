package se.umu.cs.gcom.Communication;

import se.umu.cs.gcom.GCom.Message;

import java.util.List;

public interface Communication {
    List<String> multicast(List<String> memberlist, Message message);
}
