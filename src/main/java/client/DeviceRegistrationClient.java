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
import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class DeviceRegistrationClient {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1100);
            DeviceRegistry deviceRegistry = (DeviceRegistry) registry.lookup("DeviceRegistry");

            String deviceName = "EmployeeDevice"; // يمكن أن يكون اسم الكمبيوتر
            String ip = InetAddress.getLocalHost().getHostAddress(); // يحصل على عنوان IP الحالي

            deviceRegistry.registerDevice(deviceName, ip);

            System.out.println("Device registered!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
