/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

/**
 *
 * @author hornet
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import interfaces.ScreenshotService;

public class ScreenshotServiceImpl extends UnicastRemoteObject implements ScreenshotService {
    private static final String FOLDER_PATH = "C:/Users/hornet/screenshots"; // Absolute path for saving screenshots

    public ScreenshotServiceImpl() throws RemoteException {
        super();
        // Ensure the folder exists
        File folder = new File(FOLDER_PATH);
        if (!folder.exists()) {
            folder.mkdirs(); // Create the folder if it doesn't exist
        }
    }

    @Override
    public void receiveScreenshot(byte[] screenshot) throws RemoteException {
        try {
            // Generate a unique filename based on the current timestamp
            String fileName = "screenshot_" + System.currentTimeMillis() + ".jpg";
            File outputFile = new File(FOLDER_PATH, fileName); // Create a file object

            // Save the screenshot to the specified folder
            try (FileOutputStream fos = new FileOutputStream(outputFile)) {
                fos.write(screenshot); // Write the byte array to the file
            }

            System.out.println("Screenshot saved to: " + outputFile.getAbsolutePath());

        } catch (IOException e) {
            throw new RemoteException("Error saving screenshot", e); // Rethrow as RemoteException
        }
    }
}
