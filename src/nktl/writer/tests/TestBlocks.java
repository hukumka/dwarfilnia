package nktl.writer.tests;

import nktl.math.geom.Vec3i;
import nktl.server.MinecraftRMIProcess;
import nktl.writer.RMIClient;
import nktl.writer.blocks.*;


public class TestBlocks {
    public static void main(String[] args){
        try {
            MinecraftRMIProcess process = new RMIClient().getProcess();

            new SewerTurningCorridor(0)
                    .placeAt(process, new Vec3i(0, 100, 0));
            new SewerTurningCorridor(1)
                    .placeAt(process, new Vec3i(5, 100, 0));
            new SewerTurningCorridor(2)
                    .placeAt(process, new Vec3i(5, 100, -5));
            new SewerTurningCorridor(3)
                    .placeAt(process, new Vec3i(0, 100, -5));

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
