package nktl.generator;

import nktl.math.RangeInt;
import nktl.math.geom.Direction;
import nktl.math.geom.Vec3i;

import java.util.Random;

public class DwarfMap {

    public DwarfCube[] map;

    RangeInt rangeX = new RangeInt(),
             rangeY = new RangeInt(),
             rangeZ = new RangeInt();

    int dx, dy, dz;

    public int layerSize;
    DwarfCube lastNorthExpander = null,
                lastEastExpander = null,
                lastSouthExpander = null,
                lastWestExpander = null;

    RangeInt genBoundsX = new RangeInt();
    RangeInt genBoundsY = new RangeInt();

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
        boolean has = rangeX.has(pos.x) && rangeY.has(pos.y) && rangeZ.has(pos.z);
        if (has) {
            DwarfCube cube = get(pos);
            if (cube != null && cube.typeIs(DwarfCube.TYPE_EXCLUDED))
                has = false;
        }
        return has;
    }

    // Позиция
    private int inMapPosition(Vec3i pos){
        return pos.x + pos.y*dx + pos.z*layerSize;
    }

    private Vec3i coordinates(int mapPos) {
        int z = mapPos/layerSize;
        mapPos -= z*layerSize;
        int y = mapPos/dx;
        int x = mapPos - y*dx;
        return new Vec3i(x, y, z);
    }


    public void addPointToBounds(Vec3i pos){
        genBoundsX.add(pos.x);
        genBoundsY.add(pos.y);
    }

    public void setPointAsGenBounds(Vec3i pos){
        genBoundsX.set(pos.x, pos.x);
        genBoundsY.set(pos.y, pos.y);
    }

    public boolean genBoundsMatchMax(){
        return genBoundsX.equals(rangeX) &&
                genBoundsY.equals(rangeY);
    }

    // Геты
    public DwarfCube get(Vec3i pos){
        return map[inMapPosition(pos)];
    }

    public DwarfCube get(int x, int y, int z){
        return map[inMapPosition(new Vec3i(x, y, z))];
    }

    public void placeCube(DwarfCube cube){
        map[inMapPosition(cube.position)] = cube;
    }

    public void createCubeAt(Vec3i pos) {
        createCubeAt(pos, false);
    }

    public void createCubeAt(Vec3i pos, boolean asGenBounds){
        DwarfCube theNewOne;
        map[inMapPosition(pos)] = theNewOne = new DwarfCube(pos);

        if (asGenBounds) {
            lastWestExpander = lastNorthExpander =
                            lastEastExpander = lastSouthExpander = theNewOne;
        } else
        if (!genBoundsX.has(pos.x) || !genBoundsY.has(pos.y)) {
            if (pos.y < genBoundsY.min())
                lastSouthExpander = theNewOne;
            else if (pos.y > genBoundsY.max())
                lastNorthExpander = theNewOne;
            if (pos.x < genBoundsX.min())
                lastWestExpander = theNewOne;
            else if (pos.x > genBoundsX.max())
                lastEastExpander = theNewOne;
        }
        addPointToBounds(pos);
    }

    public Vec3i getRandomPosition(int level, Random random) throws GeneratorException {
        return new Vec3i(
                RangeInt.from01(random.nextDouble(), rangeX),
                RangeInt.from01(random.nextDouble(), rangeY),
                level);
    }

    public boolean hasVerticalLaddersAtCorners(Vec3i pos) {
        Vec3i[] surroundings = {
                new Vec3i(pos.x+1, pos.y+1, pos.z),
                new Vec3i(pos.x+1, pos.y-1, pos.z),
                new Vec3i(pos.x-1, pos.y+1, pos.z),
                new Vec3i(pos.x-1, pos.y-1, pos.z)
        };

        boolean hasLadder = false;
        for (Vec3i position : surroundings) {
            if (!this.has(position)) continue;
            DwarfCube cube = get(position);
            if (cube != null && cube.typeIs(DwarfCube.TYPE_VERTICAL_LADDER)) {
                hasLadder = true;
                break;
            }
        }
        return hasLadder;
    }

    public boolean hasBlockAtDirection(Vec3i pos, Direction dir){
        Vec3i testPosition = pos.copy();
        switch (dir) {
            case NORTH:
                testPosition.y++; break;
            case SOUTH:
                testPosition.y--; break;
            case EAST:
                testPosition.x++; break;
            default:
                testPosition.x--; break;
        }
        if (!this.has(testPosition)) return false;
        return get(testPosition) != null;
    }

    public boolean hasBlocksAroundAtLevel(Vec3i pos, Direction direct) {

        Vec3i[] surroundings = {
                new Vec3i(pos.x+1, pos.y, pos.z),
                new Vec3i(pos.x-1, pos.y, pos.z),
                new Vec3i(pos.x, pos.y+1, pos.z),
                new Vec3i(pos.x, pos.y-1, pos.z)
        };
        switch (direct) {
            // Тут блэт индексы внимательно
            case EAST:
                surroundings[1] = null; break;
            case WEST:
                surroundings[0] = null; break;
            case NORTH:
                surroundings[3] = null; break;
            case SOUTH:
                surroundings[2] = null; break;
        }

        boolean hasBlock = false;

        for (Vec3i v : surroundings) {
            if (v == null) continue;
            if (this.has(v) && this.get(v) != null){
                hasBlock = true;
                break;
            }
        }
        return hasBlock;
    }

    public DwarfList toCubeList(){
        DwarfList list = new DwarfList();
        for (DwarfCube cube : map) {
            if (cube != null) {
                switch (cube.getType()) {
                    case DwarfCube.TYPE_VERTICAL_LADDER:
                    case DwarfCube.TYPE_DIAGONAL_LADDER:
                        list.get7x7().add(cube);
                        break;
                    default:
                        list.get5x5().add(cube);
                        break;
                }
            }
        }
        return list;
    }

    DwarfCube getNeighbourAt(DwarfCube cube, Direction dir){
        Vec3i pos = new Vec3i(cube.position);
        switch (dir){
            case NORTH: ++pos.y; break;
            case SOUTH: --pos.y; break;
            case EAST:  ++pos.x; break;
            case WEST:  --pos.x; break;
        }
        if (!this.has(pos)) return null;
        return get(pos);
    }

    boolean hasNeighbourAt(DwarfCube cube, Direction dir){
        Vec3i pos = new Vec3i(cube.position);
        switch (dir){
            case NORTH: ++pos.y; break;
            case SOUTH: --pos.y; break;
            case EAST:  ++pos.x; break;
            case WEST:  --pos.x; break;
        }
        return this.has(pos) && get(pos) != null;
    }

    void setCubeTypes(){
        for (DwarfCube cube : map) if (cube != null) {
            if (cube.typeIs(DwarfCube.TYPE_DIAGONAL_LADDER))
                continue;
            setCubeTypeForDir(cube, Direction.NORTH);
            setCubeTypeForDir(cube, Direction.SOUTH);
            setCubeTypeForDir(cube, Direction.EAST);
            setCubeTypeForDir(cube, Direction.WEST);
        }
    }

    private void setCubeTypeForDir(DwarfCube cube, Direction direction){
        DwarfCube neighbour = getNeighbourAt(cube, direction);
        if (neighbour != null &&
                (!neighbour.typeIs(DwarfCube.TYPE_DIAGONAL_LADDER) ||
                        neighbour.enumDirection().isParallelTo(direction))) {
            cube.addDirBit(DwarfCube.dirEnumToBit(direction));
        }
    }
}
