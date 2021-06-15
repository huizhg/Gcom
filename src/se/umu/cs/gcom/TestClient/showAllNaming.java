package se.umu.cs.gcom.TestClient;

import se.umu.cs.gcom.GroupManagement.User;
import se.umu.cs.gcom.Naming.INamingService;

import java.awt.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class showAllNaming {
    public static void main(String[] args) throws RemoteException{
        try {

            Registry registry = LocateRegistry.getRegistry(8888);
            String[] regList = registry.list();
            for (String i:regList){
                System.out.println(i);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
