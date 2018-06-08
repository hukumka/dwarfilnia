package nktl.server.tests;

import javafx.scene.control.TextField;
import nktl.server.MinecraftProcess;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;


class InputThread {

    private MinecraftProcess minecraft;
    private TextField input_field;

    InputThread(TextField input_field, MinecraftProcess minecraft) {
        this.input_field = input_field;
        this.minecraft = minecraft;
        input_field.setOnAction(e -> {
            String command = input_field.getText();
            input_field.clear();
            write(command);
        });
    }

    void write(String command) {
        try {
            minecraft.write(command);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
