/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

/**
 *
 * @author hornet
 */
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface DeviceRegistry extends Remote {
    void registerDevice(String deviceName, String ip) throws RemoteException;
    List<String> getRegisteredDevices() throws RemoteException;
}
