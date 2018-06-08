package sewer;

import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class OutputThread {

    private InputStream is;
    private TextArea output_area;
    private Thread thread;

    OutputThread(TextArea output_area, InputStream is){
        this.is = is;
        this.output_area = output_area;
        thread = new Thread(this::run);
        thread.start();
    }

    private void run(){
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String str;
            while ((str = reader.readLine()) != null){
                output_area.appendText(str + '\n');
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }


    public void stop() {
        try {
            if (thread != null)
                thread.interrupt();
        } catch (Exception e) {

        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
