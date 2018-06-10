package nktl.writer.tests;

import nktl.generator.DwarfMap;
import nktl.generator.Generator;
import nktl.math.geom.Vec3i;
import nktl.writer.MapWriter;
import nktl.writer.RMIClient;


public class TestMap {
    public static void main(String[] args){
        try {
            Generator generator = new Generator()
                    .setSeed(45825249)
                    .setLoopProbability(0.2)
                    .setLenBeforeTurn(2, 5);
            DwarfMap dm = generator.generateMap(20, 20, 1);

            MapWriter writer = new MapWriter()
                    .setProcess(new RMIClient().getProcess())
                    .setUsedPlayer("hukumka")
                    .setOffset(new Vec3i(0, 160, 0));

            writer.writeMap(dm);

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
