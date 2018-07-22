package nktl.writer.blocks;

import nktl.dwarf.DwarfCube;
import nktl.dwarf.DwarfDirection;
import nktl.math.geom.Vec3i;
import nktl.server.MinecraftRMIProcess;

import java.io.IOException;

public interface DwarfBlock {
    void placeAt(MinecraftRMIProcess process, Vec3i position) throws IOException;

    static DwarfBlock from_dwarf_cube(DwarfCube cube){
        if(cube.type() == DwarfCube.CubeType.TUNNEL) {
            return Corridor.from_dwarf_cube(cube);
        }else if(cube.type() == DwarfCube.CubeType.LADDER) {
            return VerticalLadder.from_dwarf_cube(cube);
        }else if(cube.type() == DwarfCube.CubeType.STAIRS){
            Stairs stairs = cube.features().containsKey(DwarfCube.Feature.DESTRUCTION)
                    ? new BrokenStairs()
                    : new Stairs();
            stairs.get_features(cube);
            return stairs;
        }else if(cube.type() == DwarfCube.CubeType.COLLECTOR) {
            return VerticalLadder.from_dwarf_cube(cube);
        }else{
            return null;
        }
    }
}
