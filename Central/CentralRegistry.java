package Central;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface CentralRegistry extends Remote {
    void registerClient(String clientName, String clientRegistryURL) throws RemoteException;
    void deregisterClient(String clientName) throws RemoteException;
    List<String> getActiveClients() throws RemoteException;
    String getClientRegistryURL(String clientName) throws RemoteException;
}
