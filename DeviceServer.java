import java.rmi.registry.Registry;
import org.opencv.core.Core;
import java.rmi.registry.LocateRegistry;
import java.net.InetAddress;
import java.io.FileWriter;
import java.io.PrintWriter;

public class DeviceServer {

        static {
        // Load the native OpenCV library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    public static void main(String[] args) {
        try {
            // Get the IP address of the device
            InetAddress localHost = InetAddress.getLocalHost();
            String ipAddress = localHost.getHostAddress();

            // Create the device
            Device device = new DeviceImpl(ipAddress); // Use IP address as device address

            // Get the registry or create one if it doesn't exist
            Registry registry = LocateRegistry.createRegistry(1099);

            // Bind the device's stub in the registry with unique name based on IP address
            registry.rebind(ipAddress, device);

            System.out.println("DeviceServer ready");

            // Write device information to file
            writeDeviceInfoToFile(ipAddress);
        } catch (Exception e) {
            System.err.println("DeviceServer exception: " + e.toString());
            e.printStackTrace();
        }
    }

    private static void writeDeviceInfoToFile(String ipAddress) {
        try {
            FileWriter fileWriter = new FileWriter("devices.txt", true); // Append to file
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println("Device IP: " + ipAddress);
            printWriter.close();
            System.out.println("Device information written to file");
        } catch (Exception e) {
            System.err.println("Error writing device information to file: " + e.getMessage());
        }
    }
}
