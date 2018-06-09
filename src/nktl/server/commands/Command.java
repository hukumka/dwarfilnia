package nktl.server.commands;

import nktl.server.MinecraftRMIProcess;

import java.io.IOException;

public abstract class Command {
    public abstract String toCommandString();

    public void runIn(MinecraftRMIProcess process) throws IOException {
        process.write(toCommandString());
    }
}
