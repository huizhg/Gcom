package se.umu.cs.gcom.Communication;

import se.umu.cs.gcom.GroupManagement.IGComService;
import se.umu.cs.gcom.GroupManagement.User;
import se.umu.cs.gcom.MessageOrdering.Message;
import se.umu.cs.gcom.MessageOrdering.Ordering;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class NonReliableMulticast extends Communication{

    private static final long serialVersionUID = -4095746841369138625L;

    public NonReliableMulticast() {

    }

    @Override
    public List<String> multicast(List<String> mlist, Message msg) {
        List<String> failedlist = new ArrayList<>();
        for (String m:mlist){
//            System.out.println("member = "+m);
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
