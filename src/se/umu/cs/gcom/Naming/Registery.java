package se.umu.cs.gcom.Naming;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Registery {
    public static void main(String[] args) throws InterruptedException {
        try {

            Registry registry = LocateRegistry.createRegistry(8888);
            System.out.println("Start Registry.");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to Start Registry.");
            System.exit(1);
        }
        while (true) {
            Thread.sleep(2000);
        }
    }
}
