package Central;


import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class CentralRegistryRMI {
    public static void main(String[] args) {
        try {
            CentralRegistryImpl centralRegistry = new CentralRegistryImpl();
            LocateRegistry.createRegistry(1099); // Default RMI registry port
            Naming.rebind("CentralRegistry", centralRegistry);
            System.out.println("Central Registry is ready.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
