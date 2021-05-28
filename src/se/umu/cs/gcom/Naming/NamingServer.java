package se.umu.cs.gcom.Naming;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class NamingServer {
    public static void main(String[] args) {
        try {
            INamingService namingService = new NamingService();
            // creatRegistry is different from getRegistry.
            Registry registry = LocateRegistry.createRegistry(8888);
            registry.rebind("NamingService", namingService);
            System.out.println("The name server is bound sucessfully！");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to bind the name server");
            System.exit(1);
        }
    }
}
