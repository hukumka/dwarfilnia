package nktl.dwarf;

import nktl.math.geom.Vec3i;

import java.util.Collections;
import java.util.LinkedList;

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
}
