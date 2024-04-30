/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

/**
 *
 * @author hornet
 */
import interfaces.DeviceRegistry;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class DeviceRegistryStarter {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1101); // منفذ مختلف
            DeviceRegistryImpl registryImpl = new DeviceRegistryImpl();
            registry.rebind("DeviceRegistry", registryImpl);

            System.out.println("Device Registry Service started...");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
