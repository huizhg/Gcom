package se.umu.cs.gcom.TestClient;

import se.umu.cs.gcom.Naming.INamingService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class GComServer {
    private INamingService namestub;

    public GComServer() {
        try {

            Registry registry = LocateRegistry.getRegistry(8888);
            INamingService namestub = (INamingService) registry.lookup("NamingService");
            System.out.println("GComServer starts.");
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        GComServer gComServer = new GComServer();
    }
}
