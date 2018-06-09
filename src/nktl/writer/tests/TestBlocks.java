package nktl.writer.tests;

import nktl.math.geom.Vec3i;
import nktl.server.MinecraftRMIProcess;
import nktl.writer.RMIClient;
import nktl.writer.blocks.Corridor;
import nktl.writer.blocks.VerticalLadder;
import nktl.writer.blocks.Wall;


public class TestBlocks {
    public static void main(String[] args){
        try {
            MinecraftRMIProcess process = new RMIClient().getProcess();

            new Wall().placeAt(process, new Vec3i(0, 100, 0));
            new Wall().placeAt(process, new Vec3i(0, 100, 5));
            new Wall().placeAt(process, new Vec3i(0, 100, 10));
            new Corridor().placeAt(process, new Vec3i(5, 100, 0));
            new Corridor().placeAt(process, new Vec3i(5, 100, 5));
            new Corridor().placeAt(process, new Vec3i(5, 100, 10));
            new Wall().placeAt(process, new Vec3i(10, 100, 0));
            new Wall().placeAt(process, new Vec3i(10, 100, 5));
            new Wall().placeAt(process, new Vec3i(10, 100, 10));
            new VerticalLadder().placeAt(process, new Vec3i(15, 100, 5));


        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
