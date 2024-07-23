package Central;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CentralRegistryRMI {
    public static void main(String[] args) {
        try {
            CentralRegistryImpl centralRegistry = new CentralRegistryImpl();
            Registry registry = LocateRegistry.createRegistry(1099); // Default RMI registry port
            registry.rebind("CentralRegistry", centralRegistry);
            System.out.println("Central Registry is ready.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
