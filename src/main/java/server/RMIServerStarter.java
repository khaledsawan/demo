/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

import java.rmi.RemoteException;
/**
 *
 * @author hornet
 */
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServerStarter {
    public static void main(String[] args) {
        try {
            int port = 1099; // Default RMI port
            Registry registry = LocateRegistry.createRegistry(port);

            ScreenshotServiceImpl screenshotService = new ScreenshotServiceImpl();
            registry.rebind("ScreenshotService", screenshotService);

            System.out.println("RMI server started and ScreenshotService bound to registry.");

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
