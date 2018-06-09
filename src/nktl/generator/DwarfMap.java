package nktl.generator;

import nktl.math.RangeInt;
import nktl.math.geom.Vec3i;

import java.util.LinkedList;
import java.util.Random;

public class DwarfMap {

    public DwarfCube[] map;

    RangeInt rangeX = new RangeInt(),
             rangeY = new RangeInt(),
             rangeZ = new RangeInt();

    int dx, dy, dz;

    public int layerSize;

    DwarfMap(Vec3i dimensions){
        this.dx = dimensions.x;
        this.dy = dimensions.y;
        this.dz = dimensions.z;

        rangeX.set(0, dimensions.x-1);
        rangeY.set(0, dimensions.y-1);
        rangeZ.set(0, dimensions.z-1);


        layerSize = dx * dy;

        int map_size = dimensions.x*dimensions.y*dimensions.z;
        map = new DwarfCube[map_size];
    }

    public boolean has(Vec3i pos){
        return rangeX.has(pos.x) && rangeY.has(pos.y) && rangeZ.has(pos.z);
    }

    // Позиция
    private int inMapPosition(Vec3i pos){
        return pos.x + pos.y*dx + pos.z*layerSize;
    }

    private Vec3i coordinates(int mapPos) {
        int z = mapPos/layerSize;
        mapPos -= z;
        int y = mapPos/dx;
        int x = mapPos - y;
        return new Vec3i(x, y, z);
    }


    // Геты
    public DwarfCube get(Vec3i pos){
        return map[inMapPosition(pos)];
    }

    public DwarfCube get(int x, int y, int z){
        return map[inMapPosition(new Vec3i(x, y, z))];
    }

    public void createCubeAt(Vec3i pos){
        map[inMapPosition(pos)] = new DwarfCube();
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

    public LinkedList<DwarfCube> toCubeList(){
        LinkedList<DwarfCube> cubeList = new LinkedList<>();
        for (int i = 0; i < map.length; i++) {
            if (map[i] != null){
                map[i].position = coordinates(i);
                cubeList.add(map[i]);
            }
        }
        return cubeList;
    }
}
