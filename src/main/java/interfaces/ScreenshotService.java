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

public interface ScreenshotService extends Remote {
    void receiveScreenshot(byte[] screenshotData) throws RemoteException;
}
