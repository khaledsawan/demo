/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

/**
 *
 * @author hornet
 */
import interfaces.MonitoringService;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1100);
            MonitoringServiceImpl monitoringService = new MonitoringServiceImpl();
            registry.rebind("MonitoringService", monitoringService);

            byte[] cameraImage = monitoringService.captureCameraImage();
            try (FileOutputStream fos = new FileOutputStream("camera.jpg")) {
                fos.write(cameraImage);
            }

            byte[] screenshot = monitoringService.captureScreenShot();
            try (FileOutputStream fos2 = new FileOutputStream("screenshot.jpg")) {
                fos2.write(screenshot);
            }

            System.out.println("Images saved!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
