package nktl.generator;

import nktl.math.RangeInt;
import nktl.math.geom.Vec3i;

import java.util.Random;

public class DwarfMap {

    public DwarfCube[] map;

    RangeInt rangeX = new RangeInt(),
             rangeY = new RangeInt(),
             rangeZ = new RangeInt();

    public int layerSize;

    DwarfMap(Vec3i dimensions){

        rangeX.set(0, dimensions.x-1);
        rangeY.set(0, dimensions.y-1);
        rangeZ.set(0, dimensions.z-1);

        layerSize = rangeX.span() * rangeY.span();

        int map_size = dimensions.x*dimensions.y*dimensions.z;
        map = new DwarfCube[map_size];
    }

    public boolean has(Vec3i pos){
        return rangeX.has(pos.x) && rangeY.has(pos.y) && rangeZ.has(pos.z);
    }

    public DwarfCube get(Vec3i pos){
        int position = pos.x + pos.y*rangeX.span() + pos.z*layerSize;
        return map[position];
    }

    public DwarfCube get(int x, int y, int z){
        int position = x + y*rangeX.span() + z*layerSize;
        return map[position];
    }

    public void createCubeAt(Vec3i pos){
        int position = pos.x + pos.y*rangeX.span() + pos.z*layerSize;
        map[position] = new DwarfCube();
    }

    public Vec3i getRandomPosition(int level, Random random) throws GeneratorException {
        return new Vec3i(
                RangeInt.from01(random.nextDouble(), rangeX),
                RangeInt.from01(random.nextDouble(), rangeY),
                level);
    }

    public boolean hasBlocksAroundAtLevel(Vec3i pos) {

        Vec3i[] surroundings = {
                new Vec3i(pos.x+1, pos.y, pos.z),
                new Vec3i(pos.x-1, pos.y, pos.z),
                new Vec3i(pos.x, pos.y+1, pos.z),
                new Vec3i(pos.x, pos.y-1, pos.z)
        };
        boolean hasBlock = false;

        for (Vec3i v : surroundings) {
            if (this.has(v) && this.get(v) != null){
                hasBlock = true;
                break;
            }
        }

        return hasBlock;
    }

}
