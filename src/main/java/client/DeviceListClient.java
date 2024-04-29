/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client;

/**
 *
 * @author hornet
 */
import interfaces.DeviceRegistry;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class DeviceListClient {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1100);
            DeviceRegistry deviceRegistry = (DeviceRegistry) registry.lookup("DeviceRegistry");

            List<String> devices = deviceRegistry.getRegisteredDevices();
            System.out.println("Registered Devices:");
            for (String device : devices) {
                System.out.println(device);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
