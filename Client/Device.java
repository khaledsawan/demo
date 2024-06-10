package Client;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.awt.AWTException;
import java.io.IOException;

public interface Device extends Remote {
    String captureImage() throws RemoteException, AWTException, IOException;
    String captureScreenshot() throws RemoteException, AWTException, IOException;
    String getDeviceAddress() throws RemoteException;
    void startChat() throws RemoteException; 
}