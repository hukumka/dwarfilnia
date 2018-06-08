package test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sewer.InputThread;
import sewer.OutputThread;

import java.io.IOException;

public class MainCraft extends Application {
    static String full_launch_command = "java -jar mcs_1.12.2.jar nogui";

    TextArea output_area = new TextArea();
    TextField input_field = new TextField();

    InputThread user_input;
    OutputThread server_output;
    Process process;

    @Override
    public void init() {
        try {
            process = Runtime.getRuntime().exec(full_launch_command);
            user_input = new InputThread(input_field, process.getOutputStream());
            server_output = new OutputThread(output_area, process.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) throws Exception {

        // Панелька
        BorderPane root = new BorderPane();
        root.setCenter(output_area);
        root.setBottom(input_field);

        // Запихиваем в стедж и жахаем
        stage.setScene(new Scene(root));
        stage.setWidth(800);
        stage.setHeight(600);
        stage.show();
    }

    @Override
    public void stop() {
        try {
            if (user_input != null){
                user_input.write("stop");
            }
            if (server_output != null)
                server_output.stop();
        } catch (Exception e) {

        }
    }
}
