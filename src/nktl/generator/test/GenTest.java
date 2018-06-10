package nktl.generator.test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import nktl.generator.DwarfCube;
import nktl.generator.DwarfMap;
import nktl.generator.Generator;
import nktl.math.geom.Vec3i;

import java.util.LinkedList;

public class GenTest extends Application {

    LinkedList<DwarfCube> cubeList;

    int width = 40;
    int height = 40;
    int depth = 3;

    @Override
    public void init() throws Exception {
        Generator generator = new Generator();

        DwarfCube[] ladders = {
                new DwarfCube(new Vec3i(15, 15, 0)),
                new DwarfCube(new Vec3i(25, 40, 0)),
                new DwarfCube(new Vec3i(30, 10, 0))
        };

        for (DwarfCube ladder : ladders){
            ladder.setType(DwarfCube.TYPE_VERTICAL_LADDER);
            ladder.addDirBit(DwarfCube.DATA_NORTH_BIT);
        }

        generator
                .setWayNumRelation(100, 33, 9)
                .setSeed(45825243)
                //.setSeed((long) (Math.random()*2*Long.MAX_VALUE - Long.MAX_VALUE))
                .setLoopProbability(0.2)
                .setLenBeforeTurn(3, 5);
        DwarfMap dm = generator.generateMap(width, height, depth, ladders);
        cubeList = dm.toCubeList();
        System.out.println(cubeList.size());
    }

    @Override
    public void start(Stage stage) throws Exception {
        int mult = 10;
        int w = width;
        int h = height;

        Canvas[] cs = new Canvas[depth];
        GraphicsContext[] g = new GraphicsContext[depth];

        TabPane root = new TabPane();
        for (int i = 0; i < depth; i++) {
            cs[i] = new Canvas();
            g[i] = cs[i].getGraphicsContext2D();
            Tab tab = new Tab("Level " + i, cs[i]);
            tab.setClosable(false);
            root.getTabs().add(tab);
            cs[i].setWidth(w * mult);
            cs[i].setHeight(h * mult);
            cs[i].setLayoutX(0);
            cs[i].setLayoutY(0);
            g[i].clearRect(0, 0, cs[i].getWidth(), cs[i].getHeight());
        }


        for (DwarfCube cube : cubeList){
            int level = cube.getPosition().z;
            int numWays = 0;
            if (cube.directionHas(DwarfCube.DIRECTION_NORTH_BIT)) ++numWays;
            if (cube.directionHas(DwarfCube.DIRECTION_SOUTH_BIT)) ++numWays;
            if (cube.directionHas(DwarfCube.DIRECTION_EAST_BIT)) ++numWays;
            if (cube.directionHas(DwarfCube.DIRECTION_WEST_BIT)) ++numWays;

            if (cube.typeIs(DwarfCube.TYPE_TUNNEL)) {
                switch (numWays){
                    case 1: g[level].setFill(Color.RED); break;
                    case 2: g[level].setFill(Color.YELLOW); break;
                    case 3: g[level].setFill(Color.GREEN); break;
                    case 4: g[level].setFill(Color.BLUE); break;
                    default: g[level].setFill(Color.BLACK); break;
                }
            } else if (cube.typeIs(DwarfCube.TYPE_VERTICAL_LADDER)){
                g[level].setFill(Color.LIGHTGRAY);
            }

            double x = mult*cube.getPosition().x;
            double y = mult*cube.getPosition().y;
            g[level].fillRect(x, y, mult, mult);
        }

        stage.setScene(new Scene(root));
        stage.setWidth(width*mult + 80);
        stage.setHeight(height*mult + 80);
        stage.show();
    }
}
