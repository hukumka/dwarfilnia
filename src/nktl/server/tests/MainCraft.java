package nktl.server.tests;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import nktl.server.MinecraftProcess;

import java.io.IOException;

public class MainCraft extends Application {

    private static String full_launch_command = "java -jar mcs_1.12.2.jar nogui";

    private TextArea output_area = new TextArea();
    private TextField input_field = new TextField();

    private MinecraftProcess minecraft;
    private InputThread user_input;
    private OutputThread server_output;

    @Override
    public void init() {
        try {
            minecraft = new MinecraftProcess(full_launch_command);

            user_input = new InputThread(input_field, minecraft);
            server_output = new OutputThread(output_area, minecraft.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage){
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
                user_input.write("/stop");
            }
            if (server_output != null)
                server_output.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
