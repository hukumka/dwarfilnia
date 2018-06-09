package nktl.server;

import java.io.IOException;
import java.rmi.Remote;

public interface MinecraftRMIProcess extends Remote {
    void write(String command) throws IOException;
}
