package nktl.writer.tests;

import nktl.dwarf.DwarfGen;
import nktl.dwarf.DwarfMap;
import nktl.math.geom.Vec3i;
import nktl.server.MinecraftRMIProcess;
import nktl.writer.MapWriter;
import nktl.writer.RMIClient;
import nktl.writer.blocks.Corridor;


public class TestMap {
    public static void main(String[] args){
        try {
            DwarfGen generator = new DwarfGen();

            generator.settings()
                    .setSeed(45825243)
                    .setWayRatio(100, 50, 20, 10, 5, 1)
                    .setDimensions(11, 11, 11);
            DwarfMap dm = generator.genMap();

            MapWriter writer = new MapWriter()
                    .setProcess(new RMIClient().getProcess())
                    .setUsedPlayer("hukumka")
                    .setOffset(new Vec3i(-1000, 160, -800));

            writer.writeMap(dm);
            /*
            RMIClient client = new RMIClient();
            MinecraftRMIProcess process = client.getProcess();
            for(int i=0; i<16; ++i){
                Corridor c = new Corridor();
                boolean w[] = {
                        (i&1) > 0,
                        (i&2) > 0,
                        (i&4) > 0,
                        (i&8) > 0,
                };
                c.set_ways(w);
                c.placeAt(process, new Vec3i(-600, 140, -420).plus(0, 5*i, 0));
            }
            */


        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
