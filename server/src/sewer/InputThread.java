package sewer;

import javafx.scene.control.TextField;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class InputThread {

    PrintWriter writer;
    TextField input_field;

    public InputThread(TextField input_field, OutputStream outputStream) {
        this.input_field = input_field;
        writer = new PrintWriter(new OutputStreamWriter(outputStream));

        input_field.setOnAction(e -> {
            String command = input_field.getText();
            input_field.clear();
            write(command);
        });
    }

    public void write(String command) {
        writer.println(command);
        writer.flush();
    }
}
