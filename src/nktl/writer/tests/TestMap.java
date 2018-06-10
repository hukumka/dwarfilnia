package nktl.writer.tests;

import nktl.generator.DwarfCube;
import nktl.generator.DwarfMap;
import nktl.generator.Generator;
import nktl.math.geom.Vec3i;
import nktl.writer.MapWriter;
import nktl.writer.RMIClient;


public class TestMap {
    public static void main(String[] args){
        try {
             DwarfCube[] ladders = {
                new DwarfCube(new Vec3i(15, 15, 0)),
                new DwarfCube(new Vec3i(25, 40, 0)),
                new DwarfCube(new Vec3i(30, 10, 0))
            };

            for (DwarfCube ladder : ladders){
                ladder.setType(DwarfCube.TYPE_VERTICAL_LADDER);
                ladder.addDirBit(DwarfCube.DATA_NORTH_BIT);
            }

            Generator generator = new Generator()
                    .setWayNumRelation(100, 33, 9)
                    .setSeed(45825243)
                    //.setSeed((long) (Math.random()*2*Long.MAX_VALUE - Long.MAX_VALUE))
                    .setLoopProbability(0.2)
                    .setLenBeforeTurn(3, 5);
            DwarfMap dm = generator.generateMap(40, 40, 1, ladders);

            MapWriter writer = new MapWriter()
                    .setProcess(new RMIClient().getProcess())
                    .setUsedPlayer("hukumka")
                    .setOffset(new Vec3i(0, 140, 0));

            writer.writeMap(dm);

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
