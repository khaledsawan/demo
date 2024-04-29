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
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class DeviceRegistryImpl extends UnicastRemoteObject implements DeviceRegistry {
    private List<String> devices = new ArrayList<>();

    public DeviceRegistryImpl() throws RemoteException {
        super();
    }

    @Override
    public void registerDevice(String deviceName, String ip) throws RemoteException {
        devices.add(deviceName + "@" + ip);
    }

    @Override
    public List<String> getRegisteredDevices() throws RemoteException {
        return devices;
    }
}
