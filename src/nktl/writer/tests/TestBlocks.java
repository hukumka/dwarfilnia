package nktl.writer.tests;

import nktl.generator.DwarfCube;
import nktl.math.geom.Vec3i;
import nktl.server.MinecraftRMIProcess;
import nktl.writer.RMIClient;
import nktl.writer.blocks.*;


public class TestBlocks {
    public static void main(String[] args){
        try {
            MinecraftRMIProcess process = new RMIClient().getProcess();

            new Corridor()
                    .setData(DwarfCube.DIRECTION_EAST_BIT | DwarfCube.DIRECTION_NORTH_BIT)
                    .placeAt(process, new Vec3i(0, 110, 0));
            new Corridor()
                    .setData(DwarfCube.DIRECTION_WEST_BIT | DwarfCube.DIRECTION_NORTH_BIT)
                    .placeAt(process, new Vec3i(5, 110, 0));
            new Corridor()
                    .setData(DwarfCube.DIRECTION_WEST_BIT | DwarfCube.DIRECTION_SOUTH_BIT)
                    .placeAt(process, new Vec3i(5, 110, 5));
            new Corridor()
                    .setData(DwarfCube.DIRECTION_EAST_BIT | DwarfCube.DIRECTION_SOUTH_BIT)
                    .placeAt(process, new Vec3i(0, 110, 5));

            new Corridor()
                    .setData(0x3)
                    .placeAt(process, new Vec3i(0, 100, 0));
            new Corridor()
                    .setData(0x1)
                    .placeAt(process, new Vec3i(0, 105, 0));

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
