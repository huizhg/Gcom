package se.umu.cs.gcom.TestClient;

import se.umu.cs.gcom.GroupManagement.User;
import se.umu.cs.gcom.Naming.INamingService;

import javax.sound.midi.Soundbank;
import java.io.PrintWriter;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.List;

public class testNaming {
    public static void main(String[] args) throws RemoteException{
        try {
            User u1 = new User("u1");

            System.out.println(u1.getId());
            Registry registry = LocateRegistry.getRegistry(8888);
            INamingService stub = (INamingService) registry.lookup("NamingService");
            INamingService stub1 = (INamingService) registry.lookup("NamingService");

            List<INamingService> uList = new ArrayList();
            uList.add(stub);
            uList.add(stub1);
            System.out.println(uList);
//            System.out.println(stub.equals(stub1));
//            stub.createGroup("g1",u1);
//            stub.deleteGroup("g1");
//            System.out.println(stub.getAllGroups());
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
