package nktl.writer.blocks;

import nktl.dwarf.DwarfCube;
import nktl.math.geom.Vec3i;
import nktl.server.MinecraftRMIProcess;

import java.io.IOException;
import java.util.Random;

public interface DwarfBlock {

    //private Random random;
    //
    //public DwarfBlock(Random random) {
    //    this.random = random;
    //}

    void placeAt(MinecraftRMIProcess process, Vec3i position) throws IOException;

    static DwarfBlock from_dwarf_cube(DwarfCube cube, Random random){
        if(cube.type() == DwarfCube.CubeType.TUNNEL) {
            return Corridor.from_dwarf_cube(cube);
        }else if(cube.type() == DwarfCube.CubeType.LADDER) {
            return VerticalLadder.from_dwarf_cube(cube);
        }else if(cube.type() == DwarfCube.CubeType.STAIRS){
            Stairs stairs = cube.features().containsKey(DwarfCube.Feature.DESTRUCTION)
                    ? new BrokenStairs(random)
                    : new Stairs();
            stairs.get_features(cube);
            return stairs;
        }else if(cube.type() == DwarfCube.CubeType.COLLECTOR) {
            System.out.println("Collector");
            Collector collector = new Collector();
            collector.get_features(cube);
            return collector;
        }else{
            return null;
        }
    }
}
