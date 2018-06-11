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

            /*
            for(int i=0; i<0x10; ++i) {
                new Corridor()
                        .setData(i)
                        .placeAt(process, new Vec3i(-20 - 5*i, 100, 0));
            }
            */

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
