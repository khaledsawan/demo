/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client;

/**
 *
 * @author hornet
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class DeviceSocketServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(1200); // اختر المنفذ
            System.out.println("Device Socket Server is running...");

            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String message = reader.readLine();
                    
                    System.out.println("Received message: " + message);
                    
                    // استجابة بسيطة
                    OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
                    writer.write("Message received\n");
                    writer.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
