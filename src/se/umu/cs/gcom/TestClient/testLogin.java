package se.umu.cs.gcom.TestClient;

import se.umu.cs.gcom.GroupManagement.GComService;
import se.umu.cs.gcom.GroupManagement.IGComService;
import se.umu.cs.gcom.GroupManagement.User;
import se.umu.cs.gcom.Naming.INamingService;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class testLogin {
    public static void main(String[] args) throws RemoteException{
        try {

            Registry registry = LocateRegistry.getRegistry(8888);
            System.out.println(registry.toString());
            System.out.println(Arrays.toString(registry.list()));
            IGComService stub = (IGComService) registry.lookup("q1");
            stub.getUser();

            System.out.println("Get it!");


        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
