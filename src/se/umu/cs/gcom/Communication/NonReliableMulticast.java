package se.umu.cs.gcom.Communication;

import se.umu.cs.gcom.GCom.IGComService;
import se.umu.cs.gcom.GCom.Message;

import java.io.Serializable;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class NonReliableMulticast implements Communication, Serializable {

    private static final long serialVersionUID = -4095746841369138625L;

    public NonReliableMulticast() {
    }

    @Override
    public List<String> multicast(List<String> mlist, Message msg) {
        List<String> failedlist = new ArrayList<>();
        msg.updateMsgPath("-Multicast");
        msg.setPerformance(mlist.size());
        for (String m:mlist){
            try {
                Registry registry = LocateRegistry.getRegistry(8888);
                IGComService mStub = (IGComService) registry.lookup(m);
                mStub.sendMessage(msg);
            } catch (Exception e){
                failedlist.add(m);
            }
        }
        return failedlist;
    }
}
