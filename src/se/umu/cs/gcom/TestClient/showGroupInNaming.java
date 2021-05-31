package se.umu.cs.gcom.TestClient;

import se.umu.cs.gcom.GroupManagement.User;
import se.umu.cs.gcom.Naming.INamingService;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

public class showGroupInNaming {
    public static void main(String[] args) throws RemoteException{
        try {

            Registry registry = LocateRegistry.getRegistry(8888);
            INamingService stub = (INamingService) registry.lookup("NamingService");
            HashMap userMap = stub.getUserMap();
            Iterator iter = userMap.entrySet().iterator();
            while (iter.hasNext()){
                Map.Entry entry = (Map.Entry) iter.next();
                String groupname = (String) entry.getKey();
                User leader = (User) entry.getValue();
                System.out.println("Group - "+groupname+"; Leader - "+leader.getId());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
