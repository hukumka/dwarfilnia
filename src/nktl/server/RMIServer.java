package nktl.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIServer{
    public static String name = "MinecraftRMIProcess";
    public static int port = 22035;

    MinecraftProcess process;
    Registry registry;

    public RMIServer() throws IOException {
        process = new MinecraftProcess();
        registry = LocateRegistry.createRegistry(port);
        MinecraftRMIProcess stub = (MinecraftRMIProcess) UnicastRemoteObject.exportObject(process, 0);
        registry = LocateRegistry.getRegistry("localhost",port);
        registry.rebind(name, stub);

        // write output
        Thread t = new Thread(this::write_output);
        t.run();
    }

    void write_output(){
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String str;
            while ((str = reader.readLine()) != null) {
                System.out.println(str);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException{
        RMIServer server = new RMIServer();
    }
}
