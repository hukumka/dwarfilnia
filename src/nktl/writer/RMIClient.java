package nktl.writer;

import nktl.server.MinecraftRMIProcess;
import nktl.server.RMIServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient {
    static String name = RMIServer.name;
    static int port = RMIServer.port;

    Registry registry;
    MinecraftRMIProcess process;

    public RMIClient() throws RemoteException, NotBoundException{
        registry = LocateRegistry.getRegistry("localhost", port);
        process = (MinecraftRMIProcess) registry.lookup(name);
    }

    public MinecraftRMIProcess getProcess(){
        return process;
    }
}
