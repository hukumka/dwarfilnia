package nktl.generator.test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import nktl.generator.DwarfCube;
import nktl.generator.DwarfMap;
import nktl.generator.Generator;
import nktl.generator.GeneratorException;
import nktl.math.RangeInt;

import java.util.LinkedList;
import java.util.Random;

public class GenTest extends Application {

    LinkedList<DwarfCube> cubeList;

    @Override
    public void init() throws Exception {
        Generator generator = new Generator();
        generator
                .setOneWayProbability(0.66666)
                .setSeed(45825243)
                .setLoopProbability(0.2)
                .setMaxLenBeforeTurn(5);
        DwarfMap dm = generator.generateMap(100, 100, 1);
        cubeList = dm.toCubeList();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Pane root = new Pane();
        Canvas canvas = new Canvas();
        canvas.setWidth(500);
        canvas.setHeight(500);
        canvas.setLayoutX(0);
        canvas.setLayoutY(0);
        root.getChildren().add(canvas);

        GraphicsContext g = canvas.getGraphicsContext2D();
        g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        g.setFill(Color.GREEN);
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        g.setFill(Color.GHOSTWHITE);
        for (DwarfCube cube : cubeList) if (cube.getPosition().z == 0){
            double x = 5*cube.getPosition().x;
            double y = 5*cube.getPosition().y;
            g.fillRect(x, y, 5, 5);
        }

        stage.setScene(new Scene(root));
        stage.setWidth(800);
        stage.setHeight(600);
        stage.show();
    }
}
