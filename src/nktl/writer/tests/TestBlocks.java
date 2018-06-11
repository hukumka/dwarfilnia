package nktl.writer.tests;

import nktl.generator.DwarfCube;
import nktl.math.geom.Direction;
import nktl.math.geom.Vec3i;
import nktl.server.MinecraftRMIProcess;
import nktl.writer.RMIClient;
import nktl.writer.blocks.*;


public class TestBlocks {
    public static void main(String[] args){
        try {
            MinecraftRMIProcess process = new RMIClient().getProcess();

            new Stairs()
                    .setDirection(Direction.EAST)
                    .placeAt(process, new Vec3i(20, 100, 0));
            new Stairs()
                    .setDirection(Direction.EAST)
                    .placeAt(process, new Vec3i(20, 105, 0));
            new Stairs()
                    .setDirection(Direction.WEST)
                    .placeAt(process, new Vec3i(20, 100, 5));
            new Stairs()
                    .setDirection(Direction.WEST)
                    .placeAt(process, new Vec3i(20, 105, 5));
            new Stairs()
                    .setDirection(Direction.NORTH)
                    .placeAt(process, new Vec3i(20, 100, 20));
            new Stairs()
                    .setDirection(Direction.NORTH)
                    .placeAt(process, new Vec3i(20, 105, 20));
            new Stairs()
                    .setDirection(Direction.SOUTH)
                    .placeAt(process, new Vec3i(25, 100, 20));
            new Corridor()
                    .setData(0xf)
                    .placeAt(process, new Vec3i(25, 210, 25));
            new Stairs()
                    .setDirection(Direction.SOUTH)
                    .placeAt(process, new Vec3i(25, 205, 20));
            new Stairs()
                    .setDirection(Direction.SOUTH)
                    .placeAt(process, new Vec3i(25, 200, 15));

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
