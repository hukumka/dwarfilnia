package nktl.dwarf;

import nktl.math.geom.Vec3i;
import nktl.math.graph.Graph;

import static nktl.dwarf.DwarfCube.CubeType;
import static nktl.dwarf.DwarfCube.Feature;
import static nktl.dwarf.DwarfDirection.NEG_Y;
import static nktl.dwarf.DwarfDirection.POS_Y;

public class DwarfDigger {
    enum Type {
        TUNNEL, STAIRS, LADDER, COLLECTOR, SPIRAL;
    }

    private static boolean fatAtCorners(DwarfMap map, Vec3i pos){
        Vec3i[] ps = {
                new Vec3i(pos.x+1, pos.y, pos.z+1),
                new Vec3i(pos.x-1, pos.y, pos.z+1),
                new Vec3i(pos.x+1, pos.y, pos.z-1),
                new Vec3i(pos.x-1, pos.y, pos.z-1)
        };
        for (Vec3i p : ps)
            if (map.hasPosition(p)) {
                DwarfCube cube = map.cubeAt(p);
                if (cube != null && cube.isFat())
                    return true;
            }

        return false;
    }

    private static boolean fatNeighbour(DwarfMap map, Vec3i pos){
        Vec3i[] ps = {
                new Vec3i(pos.x+1, pos.y, pos.z),
                new Vec3i(pos.x, pos.y, pos.z+1),
                new Vec3i(pos.x, pos.y, pos.z-1),
                new Vec3i(pos.x-1, pos.y, pos.z)
        };
        for (Vec3i p : ps)
            if (map.hasPosition(p)) {
                DwarfCube cube = map.cubeAt(p);
                if (cube != null && cube.isFat())
                    return true;
            }

        return false;
    }

    private static boolean sameNode(DwarfCube cube1, DwarfCube cube2){
        return cube1.node == cube2.node;
    }

    private static boolean nodesConnected(Graph<DwarfCube> graph, DwarfCube cube1, DwarfCube cube2){
        return graph.isConnected(cube1.node, cube2.node);
    }

    private static boolean cageNeighbours(DwarfMap map, DwarfSet set, DwarfCube cube, Vec3i pos, DwarfDirection...dirs){
        boolean hasNeighbours = false;
        for (DwarfDirection dir : dirs){
            DwarfCube nbr = map.getNeighbour(pos, dir);
            if (nbr == null) continue;
            hasNeighbours = true;
            if (nbr.node == cube.node) {
                if (!set.makeNodeLoop())
                    cube.addBit(DwarfCube.Feature.CAGE, dir.bit);
            } else {
                if (nodesConnected(map.graph(), nbr, cube))
                    cube.addBit(DwarfCube.Feature.CAGE, dir.bit);
                else
                    map.graph().connect(nbr.node, cube.node);
            }
        }
        return hasNeighbours;
    }

    private static boolean hasNeighboursAt(DwarfMap map, Vec3i pos, DwarfDirection...dirs){
        for (DwarfDirection dir : dirs) {
            if (map.getNeighbour(pos, dir) != null)
                return true;
        }
        return false;
    }

    private static boolean hasFatAround(DwarfMap map, Vec3i pos){
        return fatNeighbour(map, pos) || fatAtCorners(map, pos);
    }

    /*
        КОПАЕТ ПРЯМОЙ ТОННЕЛЬ В СТОРОНУ
     */
    public static Vec3i digTunnel(DwarfMap map, DwarfSet set, DwarfWay way){
        int length = set.getLength();
        if (length < 2){
            DwarfCube root = map.cubeAt(way);
            if (root.type == CubeType.LADDER || root.type == CubeType.COLLECTOR)
                length = 2;
        }

        for (int i = 0; i < length; i++) {
            way.plusIn(way.dir.increment);
            if (!map.hasPosition(way) || fatAtCorners(map, way))
                return null;
            DwarfCube cube = map.addCubeAt(way, way.node);
            if (cageNeighbours(map, set, cube, way,
                    way.dir.getFront(), way.dir.getLeft(), way.dir.getRight()))
                return null;
        }
        return way;
    }

    /*
        КОПАЕТ КОЛЛЕКТОР ВНИЗ
     */
    public static Vec3i digCollector(DwarfMap map, DwarfSet set, DwarfWay way){
        int length = set.getLength();
        int y0 = way.y;
        boolean couldNotPlace = false;
        for (int i = 0; i < length; i++) {
            way.plusIn(way.dir.increment);

            if (!map.hasPosition(way)
                    || fatAtCorners(map, way)
                    || map.cubeAt(way) != null){
                couldNotPlace = true;
                break;
            }
            DwarfCube cube = map.addCubeAt(way, way.node);
            cube.type = CubeType.COLLECTOR;
            if (cageNeighbours(map, set, cube, way,
                    DwarfDirection.getHorizontal()))
                return null;
        }
        if (couldNotPlace)
            way.plusIn(way.dir.decrement);
        DwarfCube lastCube = map.cubeAt(way);
        if (way.y == y0) return null;
        lastCube.addBit(Feature.WATER, 0);
        way.plusIn(way.dir.decrement);
        if (way.y >= y0) return null;
        DwarfCube newRoot = map.cubeAt(way);
        newRoot.node = map.graph().newNode(newRoot);
        return way;
    }

    /*
        КОПАЕТ ВЕРТИКАЛЬНУЮ ЛЕСТНИЦУ
     */
    public static Vec3i digLadder(DwarfMap map, DwarfSet set, DwarfWay way){
        int length = set.getLength();
        boolean couldNotPlace = false;
        int y0 = way.y;
        for (int i = 0; i < length; i++) {
            way.plusIn(way.dir.increment);

            if (!map.hasPosition(way)
                    || map.cubeAt(way) != null
                    || fatAtCorners(map, way)
                    || hasNeighboursAt(map, way, DwarfDirection.getHorizontal())){
                couldNotPlace = true;
                break;
            }
            DwarfCube cube = map.addCubeAt(way, way.node);
            cube.type = CubeType.LADDER;
        }
        if (couldNotPlace)
            way.plusIn(way.dir.decrement);
        return way.y == y0 ? null : way;
    }

    /*
        КОПАЕТ ДИАГОНАЛЬНУЮ ЛЕСТНИЦУ
     */
    public static Vec3i digStairs(DwarfMap map, DwarfSet set, DwarfWay way){
        Vec3i addInc = new Vec3i(0, set.random() < 0.5 ? -1 : 1, 0);
        Vec3i addDec = new Vec3i(0, -addInc.y, 0);
        int length = 2*set.getLength();
        int dirBit = addInc.y == 1 ? way.dir.bit : way.dir.getBack().bit;

        boolean digSide = true;
        boolean couldNotPlace = false;
        boolean notMapPosition = false;
        boolean hasCube = false;
        boolean fatAtCorners;
        boolean fatNeighbour;
        int y0 = way.y;
        int i;
        for (i = 0; i < length; i++) {
            way.plusIn(digSide ? way.dir.increment : addInc);
            notMapPosition = !map.hasPosition(way);
            hasCube = map.cubeAt(way) != null;
            fatAtCorners = fatAtCorners(map, way);
            fatNeighbour = fatNeighbour(map, way);
            couldNotPlace = notMapPosition || hasCube || fatAtCorners || fatNeighbour;
            if (couldNotPlace) break;
            DwarfCube cube = map.addCubeAt(way, way.node);
            cube.type = CubeType.STAIRS;
            cube.addBit(Feature.WAY, dirBit);
            cube.addBit(Feature.DESTRUCTION, digSide ? POS_Y.bit : NEG_Y.bit);
            digSide = !digSide;
        }

        if (couldNotPlace) {
            if (notMapPosition) return null;
            if (hasCube) {
                if (way.oneWay){
                    if (addInc.y > 0){
                        if (digSide) way.plusIn(way.dir.decrement);
                        way.plusIn(addDec);
                        if (digSide)
                            map.cubeAt(way).features.put(Feature.DESTRUCTION, 0);
                        else{
                            DwarfCube cube = map.cubeAt(way);
                            cube.type = CubeType.TUNNEL;
                            cube.features.remove(Feature.DESTRUCTION);
                            cube.features.remove(Feature.WAY);
                        }
                        way.plusIn(addDec);
                        if (!map.hasPosition(way)) return null;
                        if (map.cubeAt(way) != null) return null;
                        map.addNodeCubeAt(way);
                        return way;
                    } else {
                        if (digSide) {
                            way.plusIn(way.dir.decrement);
                            DwarfCube cube = map.cubeAt(way);
                            cube.type = CubeType.TUNNEL;
                            cube.features.remove(Feature.WAY);
                            cube.features.remove(Feature.DESTRUCTION);
                        }
                        return null;
                    }
                }
                if (digSide) {
                    return utilHrzObstacle(map, set, way, map.cubeAt(way));
                } else {
                    way.plusIn(addDec);
                    DwarfCube cube = map.cubeAt(way);
                    cube.type = CubeType.TUNNEL;
                    cube.features.remove(Feature.WAY);
                    cube.features.remove(Feature.DESTRUCTION);
                    return cube.position;
                }
                //return null;
            }
            if (digSide) {
                way.plusIn(way.dir.decrement);
                map.cubeAt(way).addBit(Feature.DESTRUCTION, POS_Y.bit | NEG_Y.bit);
                return null;
            } else {
                way.plusIn(addDec);
                DwarfCube cube = map.cubeAt(way);
                cube.type = CubeType.TUNNEL;
                cube.features.remove(Feature.WAY);
                cube.features.remove(Feature.DESTRUCTION);
                return cube.position;
            }
            //return null;
        } else {
            way.plusIn(way.dir.increment);
            if (!map.hasPosition(way)) return null;
            return utilHrzObstacle2(map, set, way, map.cubeAt(way));
        }
        //return null;
    }

    // Для лестниц. Замыкание на горизонтальное препятствие
    private static Vec3i utilHrzObstacle(DwarfMap map, DwarfSet set, DwarfWay way, DwarfCube obstacle){
        way.plusIn(way.dir.decrement);
        DwarfCube stairs = map.cubeAt(way);

        if ((sameNode(obstacle, stairs) && !set.makeNodeLoop())
                || nodesConnected(map.graph(), obstacle, stairs))
            stairs.addBit(Feature.DESTRUCTION, POS_Y.bit | NEG_Y.bit);
        return null;
    }

    private static Vec3i utilNormalEnd(DwarfMap map, DwarfWay way){
        if (fatAtCorners(map, way)){
            way.plusIn(way.dir.decrement);
            map.cubeAt(way).addBit(Feature.DESTRUCTION, POS_Y.bit | NEG_Y.bit);
            return null;
        }
        map.addCubeAt(way, way.node);
        return way;
    }

    private static Vec3i utilHrzObstacle2(DwarfMap map, DwarfSet set, DwarfWay way, DwarfCube obstacle){
        if (obstacle == null) {
            return utilNormalEnd(map, way);
        } else {
            // Замыкание на препятствие
            return utilHrzObstacle(map, set, way, obstacle);
        }
    }





    /*

     */
    public static DwarfMap dig() throws GeneratorException {

        DwarfGen gen = new DwarfGen();
        gen.settings()
                .setSeed(0)
                .setWayRatio(50, 50, 50, 50, 10, 5)
                .setDimensions(21, 21, 21);
        return gen.genMap(new Vec3i(11, 11, 11));
    }

}
