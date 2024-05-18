import java.rmi.Remote;
import java.rmi.RemoteException;
import java.awt.AWTException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Socket;

public interface Device extends Remote {
    String captureImage() throws RemoteException, AWTException, IOException;
    String captureScreenshot() throws RemoteException, AWTException, IOException;
    String getDeviceAddress() throws RemoteException;
    void startChat() throws RemoteException; 
}