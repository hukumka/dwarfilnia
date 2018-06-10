package nktl.writer.tests;

import nktl.math.geom.Vec3i;
import nktl.server.MinecraftRMIProcess;
import nktl.writer.RMIClient;
import nktl.writer.blocks.*;


public class TestBlocks {
    public static void main(String[] args){
        try {
            MinecraftRMIProcess process = new RMIClient().getProcess();

            new Corridor()
                    .placeAt(process, new Vec3i(0, 100, 0));

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
