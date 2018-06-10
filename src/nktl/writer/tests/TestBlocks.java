package nktl.writer.tests;

import nktl.math.geom.Vec3i;
import nktl.server.MinecraftRMIProcess;
import nktl.writer.RMIClient;
import nktl.writer.blocks.Corridor;
import nktl.writer.blocks.SewerStraitCorridor;
import nktl.writer.blocks.VerticalLadder;
import nktl.writer.blocks.Wall;


public class TestBlocks {
    public static void main(String[] args){
        try {
            MinecraftRMIProcess process = new RMIClient().getProcess();

            new SewerStraitCorridor(true)
                    .placeAt(process, new Vec3i(0, 100, 0));
            new SewerStraitCorridor(false)
                    .placeAt(process, new Vec3i(10, 100, 0));


        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
