package nktl.dwarf;

import nktl.math.geom.Vec3i;

import java.util.Collections;
import java.util.LinkedList;

import static nktl.dwarf.DwarfCube.TYPE_ROOT_CUBE;

public class DwarfGen {

    private DwarfSet set = new DwarfSet();

    /*
        PUBLIC
     */
    public DwarfSet getSet(){
        return set;
    }

    public DwarfMap genMap(Vec3i dim, Vec3i root) throws GeneratorException {
        if (!dimIsValid(dim)) throw new GeneratorException("Invalid dimensions : " + dim);
        var map = new DwarfMap(dim);

        if (root == null || !map.hasPosition(root))
            root = map.getRandomPosition(set);

        var rootCube = map.addCubeAt(root, TYPE_ROOT_CUBE);
        var rootNode = map.graph().newNode(rootCube);
        rootCube.setNode(rootNode);
        var way = new DwarfWay(rootNode, root, 0);
        way.copy(root);

        var ways = new LinkedList<DwarfWay>();
        ways.add(way);
        do {
            ways = genWays(map, ways);
            if (ways.isEmpty())
                ways = checkBorders(map);
            drawWays(map, ways);
        } while(!ways.isEmpty());


        return map;
    }

    // Заставляет все пути нарисоваться
    private void drawWays(DwarfMap map, LinkedList<DwarfWay> ways) throws GeneratorException {
        for (var way : ways)
            way.drawWay(map, set);
    }

    private LinkedList<DwarfWay> checkBorders(DwarfMap map) throws GeneratorException {
        var newWays = new LinkedList<DwarfWay>();
        DwarfWay[] ways = map.getEdgeWays();
        for (var way : ways) {
            DwarfWay[] ws = way.genWays(map, set);
            Collections.addAll(newWays, ws);
        }
        return newWays;
    }

    // Заставляет все мути сгенерить новые пути и скидывает их в новый лист
    private LinkedList<DwarfWay> genWays(DwarfMap map, LinkedList<DwarfWay> ways) throws GeneratorException {
        var newWays = new LinkedList<DwarfWay>();
        for (var way : ways) {
            DwarfWay[] ws = way.genWays(map, set);
            Collections.addAll(newWays, ws);
        }
        ways.clear();
        return newWays;
    }


    /*
        PRIVATE
     */
    private boolean dimIsValid(Vec3i dim){
        return dim.x > 0 && dim.y > 0 && dim.z > 0;
    }

    /*
        STATIC
     */


}
