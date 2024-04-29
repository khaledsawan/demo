/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;

/**
 *
 * @author hornet
 */
import interfaces.MonitoringService;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import javax.imageio.ImageIO;

public class MonitoringServiceImpl extends UnicastRemoteObject implements MonitoringService {
    public MonitoringServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public byte[] captureCameraImage() throws RemoteException {
        // مثال لالتقاط صورة من كاميرا، قد تحتاج إلى مكتبة OpenCV أو غيرها
        // إليك كيفية تحويل BufferedImage إلى بايتات
        BufferedImage image = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
        // قم بمحاكاة التقاط الصورة هنا

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", baos);
        } catch (IOException e) {
            throw new RemoteException("Error capturing camera image", e);
        }

        return baos.toByteArray();
    }

    @Override
    public byte[] captureScreenShot() throws RemoteException {
        try {
            Robot robot = new Robot();
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screenshot = robot.createScreenCapture(screenRect);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(screenshot, "jpg", baos);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RemoteException("Error capturing screenshot", e);
        }
    }
}
