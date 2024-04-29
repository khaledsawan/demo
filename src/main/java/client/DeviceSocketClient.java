/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client;

/**
 *
 * @author hornet
 */
import java.io.OutputStreamWriter;
import java.net.Socket;

public class DeviceSocketClient {
    public static void main(String[] args) {
        try {
            String deviceIP = "127.0.0.1"; // استخدم عنوان IP الفعلي
            int port = 1200; // المنفذ الذي يتصل به
            try (Socket socket = new Socket(deviceIP, port)) {
                OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
                writer.write("Hello from Manager\n");
                writer.flush();
                
                System.out.println("Message sent!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}