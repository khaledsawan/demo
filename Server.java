import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

public class Server {
    private static final List<ClientHandler> clients = new ArrayList<>();
    private static ClientHandler selectedClient = null;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            System.out.println("Server started. Waiting for clients to connect...");

            // Start a thread to listen for server console input
            new Thread(() -> {
                try {
                    BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
                    String inputLine;
                    while ((inputLine = consoleReader.readLine()) != null) {
                        if (selectedClient != null) {
                            if (inputLine.equals("exit")) {
                                selectedClient.sendMessage("Server has exited the chat. Type 'exit' to return to client selection.");
                                selectedClient = null;
                                System.out.println("Chat ended.");
                            } else {
                                selectedClient.sendMessage(inputLine);
                            }
                        } else {
                            selectClient(inputLine);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            while (true) {
                // Accept client connection
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket);

                // Create a new ClientHandler thread for this client
                ClientHandler handler = new ClientHandler(clientSocket);
                clients.add(handler);
                handler.start();

                // Update client list in server console
                printClientList();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printClientList() {
        System.out.println("Connected Clients:");
        for (int i = 0; i < clients.size(); i++) {
            System.out.println((i + 1) + ": " + clients.get(i).getClientSocket());
        }
    }

    private static void selectClient(String input) {
        try {
            int clientNumber = Integer.parseInt(input);
            if (clientNumber > 0 && clientNumber <= clients.size()) {
                selectedClient = clients.get(clientNumber - 1);
                System.out.println("Selected client: " + selectedClient.getClientSocket());
                selectedClient.sendMessage("You have been selected for chat. Type 'exit' to end the chat.");
            } else {
                System.out.println("Invalid client number. Please enter a valid client number.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid client number.");
        }
    }

    static class ClientHandler extends Thread {
        private final Socket clientSocket;
        private BufferedReader reader;
        private PrintWriter writer;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        public Socket getClientSocket() {
            return clientSocket;
        }

        public void sendMessage(String message) {
            writer.println(message);
        }

        public void run() {
            try {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                writer = new PrintWriter(clientSocket.getOutputStream(), true);

                // Read and print messages from client
                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    if (selectedClient == this) {

                        if (inputLine != null && inputLine.startsWith("screenshot:")) {
                           // Extract Base64 string from the message
                            String screenshotBase64 = inputLine.substring("screenshot:".length());
                            // Decode Base64 string and save the screenshot as PNG file
                            byte[] screenshotBytes = Base64.getDecoder().decode(screenshotBase64);
                            BufferedImage screenshot = ImageIO.read(new ByteArrayInputStream(screenshotBytes));
                            
                            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                            File outputFile = new File("screenshots/screenshot"+timeStamp+".png");
                            ImageIO.write(screenshot, "png", outputFile);
                        }
                        else if (inputLine != null && inputLine.startsWith("photo:")) {
                            // Extract Base64 string from the message
                             String screenshotBase64 = inputLine.substring("photo:".length());
                             // Decode Base64 string and save the screenshot as PNG file
                             byte[] screenshotBytes = Base64.getDecoder().decode(screenshotBase64);
                             BufferedImage screenshot = ImageIO.read(new ByteArrayInputStream(screenshotBytes));
                             
                             String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                             File outputFile = new File("photos/photo"+timeStamp+".png");
                             ImageIO.write(screenshot, "png", outputFile);
                         } else {
                            // Handle regular chat message
                            System.out.println("Client: " + inputLine);
                        }
                    } else {
                        writer.println("You are not selected for chat. Please wait.");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    reader.close();
                    writer.close();
                    clientSocket.close();
                    clients.remove(this);
                    if (selectedClient == this) {
                        selectedClient = null;
                        System.out.println("Chat ended with client: " + clientSocket);
                    }
                    printClientList();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
