/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

/**
 *
 * @author hornet
 */
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import interfaces.ScreenshotService;

public class ScreenshotServiceImpl extends UnicastRemoteObject implements ScreenshotService {
    private List<byte[]> screenshots = new ArrayList<>();

    public ScreenshotServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public void receiveScreenshot(byte[] screenshotData) throws RemoteException {
        screenshots.add(screenshotData); // Store the screenshot
        System.out.println("Received screenshot, total count: " + screenshots.size());
    }
}
