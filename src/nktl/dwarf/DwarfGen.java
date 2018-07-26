package nktl.dwarf;

import nktl.math.geom.Vec3i;

import java.util.Collections;
import java.util.LinkedList;

import static nktl.dwarf.DwarfCube.CubeType.*;
import static nktl.dwarf.DwarfDirection.NEG_Y;
import static nktl.dwarf.DwarfDirection.POS_Y;

public class DwarfGen {

    /*
        ДАННЫЕ
     */

    private DwarfSet set = new DwarfSet(); // Настройки

    /*
        PUBLIC
     */
    public DwarfMap genMap(Vec3i... roots) throws GeneratorException {
        set.resetSeed();
        DwarfMap map = new DwarfMap(set.dimensions);
        if (roots.length == 0)
            roots = new Vec3i[] { map.randomPos(set) };
        createCubes(map, roots);
        var ways = asWays(map, roots);

        do {
            ways = continueWays(map, ways);
            //if (ways.isEmpty())
            //    ways = getBorderWays(map, ways);
        } while (!ways.isEmpty());

        for (DwarfCube cube : map.array())
            if (cube != null)
                fixWaysAndType(map, cube);

        return map;
    }

    public DwarfSet settings(){
        return set;
    }

    /*
        PRIVATE
     */

    // Достает пути с границ карты
    private LinkedList<DwarfWay> getBorderWays(DwarfMap map, LinkedList<DwarfWay> ways) throws GeneratorException {
        return asWays(ways, map, map.getEdges());
    }

    // Отрисовывает пути и создает следующие
    private LinkedList<DwarfWay> continueWays(DwarfMap map, LinkedList<DwarfWay> ways) throws GeneratorException {
        Vec3i[]positions = new Vec3i[ways.size()];
        int i = 0;
        for (DwarfWay way : ways) {
            positions[i] = way.drawWayNGetEnd(map, set);
            ++i;
        }
        ways.clear();
        return asWays(ways, map, positions);
    }

    // Создает кубы на указанных позициях
    private DwarfCube[] createCubes(DwarfMap map, Vec3i[]positions) {
        DwarfCube[]arr = new DwarfCube[positions.length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = map.addNodeCubeAt(positions[i]);
        }
        return arr;
    }

    // Создает новые пути из массива перечисленных позиций
    private LinkedList<DwarfWay> asWays(LinkedList<DwarfWay> dst, DwarfMap map, Vec3i[]positions) throws GeneratorException {
        for (Vec3i pos : positions) {
            if (pos == null) continue;
            DwarfWay[]ways = DwarfWay.newWaysAt(map, pos, set);
            Collections.addAll(dst, ways);
        }
        return dst;
    }

    private LinkedList<DwarfWay> asWays(DwarfMap map, Vec3i[]positions) throws GeneratorException {
        return asWays(new LinkedList<>(), map, positions);
    }


    /*
        ФИКСЕРЫ БИТОВ
     */
    private void fixWaysAndType(DwarfMap map, DwarfCube cube){
        switch (cube.type){
            case UNKNOWN:
                cube.type = TUNNEL;
            case TUNNEL:
                fixTunnelBits(map, cube);
                break;
            case LADDER:
                fixLadderBits(map, cube);
                break;
            case COLLECTOR:
                fixCollectorBits(map, cube);
                break;
            case STAIRS:
                //if (up!= null && up.type == STAIRS)
                break;
        }
    }

    private static void fixTunnelBits(DwarfMap map, DwarfCube cube){
        for (DwarfDirection dir : DwarfDirection.getHorizontal()){
            DwarfCube nbr = map.getNeighbour(cube.position, dir);
            if (nbr == null) continue;
            if (nbr.type == STAIRS){
                int stDir = nbr.features.get(DwarfCube.Feature.WAY);
                if (stDir != dir.bit && stDir != dir.getBack().bit)
                    continue;
            }
            if (nbr.type == COLLECTOR && nbr.features.containsKey(DwarfCube.Feature.WATER))
                continue;
            cube.addBit(DwarfCube.Feature.WAY, dir.bit);
        }
        DwarfCube up = map.getNeighbour(cube.position, POS_Y);
        DwarfCube down = map.getNeighbour(cube.position, NEG_Y);
        if (up != null && up.type == STAIRS)
            cube.addBit(DwarfCube.Feature.WAY, POS_Y.bit);
        if (down != null && down.type == STAIRS)
            cube.addBit(DwarfCube.Feature.WAY, NEG_Y.bit);
    }

    private static void fixLadderBits(DwarfMap map, DwarfCube cube) {
        for (DwarfDirection dir : DwarfDirection.getHorizontal()){
            DwarfCube nbr = map.getNeighbour(cube.position, dir);
            if (nbr == null) continue;
            //if (!nbr.hasBit(DwarfCube.Feature.CAGE, dir.getBack().bit) &&
            //        !nbr.hasBit(DwarfCube.Feature.DOOR, dir.getBack().bit)
            //        )
            cube.addBit(DwarfCube.Feature.WAY, dir.bit);
        }
        DwarfCube up = map.getNeighbour(cube.position, POS_Y);
        DwarfCube down = map.getNeighbour(cube.position, NEG_Y);
        boolean hasVertical = false;
        if (up != null)
            if (up.type == LADDER) {
                cube.addBit(DwarfCube.Feature.WAY, POS_Y.bit);
                hasVertical = true;
            }
        if (down!= null)
            if (down.type == LADDER || down.type == COLLECTOR) {
                cube.addBit(DwarfCube.Feature.WAY, NEG_Y.bit);
                hasVertical = true;
            }
        if (!hasVertical)
            cube.type = TUNNEL;
    }

    private static void fixCollectorBits(DwarfMap map, DwarfCube cube){
        if (cube.features().containsKey(DwarfCube.Feature.WATER))
            cube.features.put(DwarfCube.Feature.WAY, POS_Y.bit);
        else {
            for (DwarfDirection dir : DwarfDirection.getHorizontal()){
                DwarfCube nbr = map.getNeighbour(cube.position, dir);
                if (nbr == null) continue;
                if (!nbr.hasBit(DwarfCube.Feature.CAGE, dir.getBack().bit) &&
                        !nbr.hasBit(DwarfCube.Feature.DOOR, dir.getBack().bit)
                        ) cube.addBit(DwarfCube.Feature.WAY, dir.bit);
            }
            DwarfCube up = map.getNeighbour(cube.position, POS_Y);
            DwarfCube down = map.getNeighbour(cube.position, NEG_Y);
            boolean hasVertical = false;
            if (up != null)
                if (up.type == COLLECTOR || up.type == LADDER){
                    cube.addBit(DwarfCube.Feature.WAY, POS_Y.bit);
                    hasVertical = true;
                }
            if (down!= null)
                if (down.type == COLLECTOR){
                    cube.addBit(DwarfCube.Feature.WAY, NEG_Y.bit);
                    hasVertical = true;
                }
            if (!hasVertical)
                cube.type = TUNNEL;
        }
    }
}
