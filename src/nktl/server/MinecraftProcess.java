package nktl.server;

import java.io.*;

public class MinecraftProcess {
    private BufferedWriter output;
    private Process process;

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
            write("/stop");
            waitFor();
        }
    }

    public void write(String command) throws IOException{
        if(is_running()) {
            output.write(command + "\n");
            output.flush();
        }
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
