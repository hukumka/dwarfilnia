package nktl.writer.tests;

import nktl.generator.DwarfMap;
import nktl.generator.Generator;
import nktl.generator.GeneratorException;
import nktl.math.geom.Vec3i;
import nktl.writer.MapWriter;
import nktl.writer.RMIClient;

import java.io.IOException;

public class TestMap {
    public static void main(String[] args){
        try {
            Generator generator = new Generator()
                    .setOneWayProbability(0.66666)
                    .setSeed(45825249)
                    .setLoopProbability(0.2)
                    .setMaxLenBeforeTurn(5);
            DwarfMap dm = generator.generateMap(100, 100, 1);

            MapWriter writer = new MapWriter()
                    .setProcess(new RMIClient().getProcess())
                    .setOffset(new Vec3i(0, 200, 0));

            writer.writeMap(dm);

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
