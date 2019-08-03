package nktl.server;

import nktl.server.commands.Command;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

public class MinecraftProcess implements MinecraftRMIProcess{
    private BufferedWriter output;
    private Process process;

    public MinecraftProcess() throws IOException{
        String command = "java -jar server.jar nogui";
        System.out.println(command);
        process = Runtime.getRuntime().exec(command);
        output = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
    }

    public MinecraftProcess(String command) throws IOException{
        System.out.println(command);
        process = Runtime.getRuntime().exec(command);
        output = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
    }

    public InputStream getInputStream(){
        return process.getInputStream();
    }

    public void stop() throws IOException{
        if(is_running()) {
            write("stop");
            waitFor();
        }
    }

    public void write(String command) throws IOException{
        if(is_running()) {
            output.write(command + "\n");
            output.flush();
        }
    }

    public void execute(Command command) throws IOException{
        String cmd = command.toCommandString();
        System.err.println(cmd);
        write(cmd);
    }

    public boolean is_running(){
        return process.isAlive();
    }

    public void waitFor(){
        try {
            process.waitFor();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
