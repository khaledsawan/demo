package Central;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

public class CentralRegistryImpl extends UnicastRemoteObject implements CentralRegistry {
    private Map<String, String> clientRegistryMap;

    protected CentralRegistryImpl() throws RemoteException {
        clientRegistryMap = new HashMap<>();
    }

    @Override
    public synchronized void registerClient(String clientName, String clientRegistryURL) throws RemoteException {
        clientRegistryMap.put(clientName, clientRegistryURL);
    }

    @Override
    public synchronized void deregisterClient(String clientName) throws RemoteException {
        clientRegistryMap.remove(clientName);
    }

    @Override
    public synchronized List<String> getActiveClients() throws RemoteException {
        return new ArrayList<>(clientRegistryMap.keySet());
    }

    @Override
    public synchronized String getClientRegistryURL(String clientName) throws RemoteException {
        return clientRegistryMap.get(clientName);
    }
}
