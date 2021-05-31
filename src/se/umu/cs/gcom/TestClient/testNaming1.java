package se.umu.cs.gcom.TestClient;

import se.umu.cs.gcom.GroupManagement.User;
import se.umu.cs.gcom.Naming.INamingService;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

public class testNaming1 {
    public static void main(String[] args) throws RemoteException{
        try {

            Registry registry = LocateRegistry.getRegistry(8888);
            INamingService stub = (INamingService) registry.lookup("NamingService");

            System.out.println(stub.getAllGroups());
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
