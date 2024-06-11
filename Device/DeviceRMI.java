package Device;
import org.opencv.core.Core;
import Central.CentralRegistry;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.net.ServerSocket;
import java.io.IOException;

public class DeviceRMI {
    static {
        // Load the native OpenCV library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private static final String CENTRAL_REGISTRY_IP = "localhost";
    private static final Integer CENTRAL_REGISTRY_PORT = 1099;
    private static final String CENTRAL_REGISTRY_NAME = "CentralRegistry";

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry(CENTRAL_REGISTRY_IP, CENTRAL_REGISTRY_PORT);
            CentralRegistry centralRegistry = (CentralRegistry) Naming.lookup(CENTRAL_REGISTRY_NAME);
            String clientName = "Client" + System.currentTimeMillis(); // Unique identifier for the client
            int port = findAvailablePort();
            
            String deviceIp = "127.0.0.1"+":"+port;
            
            // String clientRegistryURL = "rmi://localhost:" + port + "/" + clientName;

            // Register the client with the central registry
            centralRegistry.registerClient(clientName, deviceIp);
            System.out.println("Client registered: " + clientName);

            // Start the client's RMI registry and bind some service
            registry = LocateRegistry.createRegistry(port);
            Device clientService = new DeviceImpl();
            registry.rebind(clientName, clientService);

            // On shutdown, deregister the client
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    centralRegistry.deregisterClient(clientName);
                    System.out.println("Client deregistered: " + clientName);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }));

            System.out.println("DeviceServer ready");

        } catch (Exception e) {
            System.err.println("DeviceServer exception: " + e.toString());
            e.printStackTrace();
        }
    }


    public static int findAvailablePort() {
        for (int port = 49152; port <= 65535; port++) { // Range for dynamic/private ports
            if (isPortAvailable(port)) {
                return port;
            }
        }
        throw new RuntimeException("No available port found in the range 49152-65535");
    }

    private static boolean isPortAvailable(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    
}
