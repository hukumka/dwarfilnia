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

import java.util.LinkedList;

public class GenTest extends Application {

    LinkedList<DwarfCube> cubeList;

    int width = 100;
    int height = 100;

    @Override
    public void init() throws Exception {
        Generator generator = new Generator();
        generator
                .setWayNumRelation(100, 33, 9)
                //.setSeed(45825243)
                .setSeed((long) (Math.random()*2*Long.MAX_VALUE - Long.MAX_VALUE))
                .setLoopProbability(0.2)
                .setLenBeforeTurn(3, 5);
        DwarfMap dm = generator.generateMap(width, height, 1);
        cubeList = dm.toCubeList();
        System.out.println(cubeList.size());
    }

    @Override
    public void start(Stage stage) throws Exception {
        int mult = 10;
        int w = width;
        int h = height;


        Pane root = new Pane();
        Canvas canvas = new Canvas();
        canvas.setWidth(w * mult);
        canvas.setHeight(h * mult);
        canvas.setLayoutX(0);
        canvas.setLayoutY(0);
        root.getChildren().add(canvas);

        GraphicsContext g = canvas.getGraphicsContext2D();
        g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        g.setFill(Color.WHITE);
        g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (DwarfCube cube : cubeList) if (cube.getPosition().z == 0){
            int numWays = 0;
            if (cube.directionHas(DwarfCube.DIRECTION_NORTH_BIT)) ++numWays;
            if (cube.directionHas(DwarfCube.DIRECTION_SOUTH_BIT)) ++numWays;
            if (cube.directionHas(DwarfCube.DIRECTION_EAST_BIT)) ++numWays;
            if (cube.directionHas(DwarfCube.DIRECTION_WEST_BIT)) ++numWays;

            switch (numWays){
                case 1: g.setFill(Color.RED); break;
                case 2: g.setFill(Color.YELLOW); break;
                case 3: g.setFill(Color.GREEN); break;
                case 4: g.setFill(Color.BLUE); break;
                default: g.setFill(Color.BLACK); break;
            }

            double x = mult*cube.getPosition().x;
            double y = mult*cube.getPosition().y;
            g.fillRect(x, y, mult, mult);
        }

        stage.setScene(new Scene(root));
        stage.setWidth(canvas.getWidth() + 80);
        stage.setHeight(canvas.getHeight() + 80);
        stage.show();
    }
}
