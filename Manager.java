import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.SimpleDateFormat;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.Base64;
import java.util.Date;

public class Manager {
    private static BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in)); // Make scanner a class variable

    public static void main(String[] args) throws IOException {
        while (true) {
            // Display available devices
            displayDevices();

            // Choose a device
            System.out.print("\nEnter the number of the device to interact with (or 0 to refresh the list): ");
       

            String input = scanner.readLine();

            // Parse the string input to an integer
            int selectedDeviceNumber = Integer.parseInt(input);
    

            if (selectedDeviceNumber == 0) continue;

            String selectedDeviceIp = getDeviceIpByNumber(selectedDeviceNumber);

            // Connect to selected device
            try {
                Registry registry = LocateRegistry.getRegistry(selectedDeviceIp);
                Device device = (Device) registry.lookup(selectedDeviceIp);

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
        try (BufferedReader br = new BufferedReader(new FileReader("devices.txt"))) {
            String line;
            int deviceCount = 0;
            System.out.println("Available Devices:");
            while ((line = br.readLine()) != null) {
                deviceCount++;
                System.out.println(deviceCount + ". " + line);
            }
        } catch (IOException e) {
            System.err.println("Error reading device information: " + e.getMessage());
        }
    }

    private static String getDeviceIpByNumber(int number) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("devices.txt"));
        try {
            String line;
            int currentNumber = 0;
            while ((line = br.readLine()) != null) {
                currentNumber++;
                if (currentNumber == number) {
                    String[] parts = line.split(": ");
                    if (parts.length > 1) {
                        return parts[1];
                    }
                }
            }
        } finally {
            br.close();
        }
        throw new IllegalArgumentException("Device not found for number: " + number);
    }

    private static void saveImageToFile(String screenshotBase64, String path) {
        try {
            byte[] screenshotBytes = Base64.getDecoder().decode(screenshotBase64);
            BufferedImage screenshot = ImageIO.read(new ByteArrayInputStream(screenshotBytes));

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File outputFile = new File(path + "/" + timeStamp + ".png");
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
