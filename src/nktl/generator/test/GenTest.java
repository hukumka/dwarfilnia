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
                .setWayNumRelation(100, 33, 9)
                //.setSeed(45825243)
                .setSeed((long) (Math.random()*2*Long.MAX_VALUE - Long.MAX_VALUE))
                .setLoopProbability(0.2)
                .setLenBeforeTurn(3, 5);
        DwarfMap dm = generator.generateMap(100, 100, 1);
        cubeList = dm.toCubeList();
        System.out.println(cubeList.size());
    }

    @Override
    public void start(Stage stage) throws Exception {
        int mult = 10;
        int w = 100;
        int h = 100;


        Pane root = new Pane();
        Canvas canvas = new Canvas();
        canvas.setWidth(w * mult);
        canvas.setHeight(h * mult);
        canvas.setLayoutX(0);
        canvas.setLayoutY(0);
        root.getChildren().add(canvas);

        GraphicsContext g = canvas.getGraphicsContext2D();
        g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        g.setFill(Color.GREEN);
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        g.setFill(Color.GHOSTWHITE);
        for (DwarfCube cube : cubeList) if (cube.getPosition().z == 0){
            double x = mult*cube.getPosition().x;
            double y = mult*cube.getPosition().y;
            g.fillRect(x, y, mult, mult);
        }

        stage.setScene(new Scene(root));
        stage.setWidth(1080);
        stage.setHeight(1080);
        stage.show();
    }
}
