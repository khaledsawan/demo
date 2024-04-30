/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client;

/**
 *
 * @author hornet
 */
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;

import interfaces.ScreenshotService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ScreenshotClient {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            ScreenshotService screenshotService = (ScreenshotService) registry.lookup("ScreenshotService");

            Robot robot = new Robot();
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());

            while (true) {
                BufferedImage screenshot = robot.createScreenCapture(screenRect);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(screenshot, "jpg", baos);
                byte[] screenshotData = baos.toByteArray();

                screenshotService.receiveScreenshot(screenshotData); // Call the correct method

                Thread.sleep(10_000); // 10 seconds
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
