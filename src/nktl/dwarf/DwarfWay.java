package nktl.dwarf;

import nktl.math.geom.Vec3i;
import nktl.math.graph.Graph;

import java.util.LinkedList;

import static nktl.dwarf.DwarfCube.CubeType.*;

class DwarfWay extends Vec3i {

    /*
        ДАННЫЕ
     */
    Graph<DwarfCube>.Node node = null;
    DwarfDirection dir;
    boolean oneWay = false;
    boolean startsNearVertical = false;


    /*
        PACKAGE-PRIVATE
     */
    DwarfWay(Vec3i pos, DwarfDirection direction){
        this.copy(pos);
        this.dir = direction;
    }

    Vec3i drawWayNGetEnd(DwarfMap map, DwarfSet set) {
        DwarfDigger.Type type;
        DwarfCube cube = map.cubeAt(this);
        this.node = cube.node;
        if (dir.isHorizontal) {
            if (cube.type == LADDER || cube.type == COLLECTOR)
                type = DwarfDigger.Type.TUNNEL;
            else
                type = set.makeStairs() ?
                        (set.makeSpiralStairs() ?
                                DwarfDigger.Type.SPIRAL : DwarfDigger.Type.STAIRS)
                        : DwarfDigger.Type.TUNNEL;
        } else {
            if (dir == DwarfDirection.POS_Y) type = DwarfDigger.Type.LADDER;
            else type = oneWay ? DwarfDigger.Type.COLLECTOR : DwarfDigger.Type.LADDER;
        }

        // TODO: 19.07.2018
        //System.out.println(String.format("Роем тоннель типа %s из точки %s", type, this));
        switch (type) {
            case TUNNEL:
                return DwarfDigger.digTunnel(map, set, this);
            case COLLECTOR:
                return DwarfDigger.digCollector(map, set, this);
            case LADDER:
                return DwarfDigger.digLadder(map, set, this);
            case STAIRS:
                return DwarfDigger.digStairs(map, set, this);
            default:
                //System.out.println("Пропускаю тоннель типа " + type);
                return null;
        }
    }

    /*
        STATIC
     */
    // Создает новые пути из точки
    static DwarfWay[] newWaysAt(DwarfMap map, Vec3i pos, DwarfSet set) throws GeneratorException {

        var possibleDirs = getPossibleWays(map, pos);
        int numOfWays = set.numWaysOfMax(possibleDirs.length);
        if (numOfWays == 0) return new DwarfWay[0];
        var actualDirs = getRandomDirs(set, possibleDirs, numOfWays);
        DwarfWay[]ways = asWays(pos, actualDirs);
        for (DwarfWay way : ways)
            way.oneWay = set.makeOneWay();

        fixCubeTypeAt(map, pos, ways);


        return ways;
    }

    private static void fixCubeTypeAt(DwarfMap map, Vec3i pos, DwarfWay[]ways){
        boolean makeCollector = false;
        for (DwarfWay way : ways) {
            if (way.dir == DwarfDirection.POS_Y || (way.dir == DwarfDirection.NEG_Y && !way.oneWay)){
                map.cubeAt(pos).type = LADDER;
                return;
            }
            if (way.dir == DwarfDirection.NEG_Y) makeCollector = true;
        }
        if (makeCollector)
            map.cubeAt(pos).type = COLLECTOR;
    }

    // Этот и следующий: проверяют свободные пути
    private static boolean dirIsClear(DwarfMap map, Vec3i pos, DwarfDirection dir){
        Vec3i neighbour = pos.plus(dir.increment);
        if (!map.hasPosition(neighbour)) return false;
        return map.cubeAt(neighbour) == null;
    }

    private static DwarfDirection[] getPossibleWays(DwarfMap map, Vec3i pos) {
        var dirList = new LinkedList<DwarfDirection>();
        boolean addVert = true;
        for (var side : DwarfDirection.getHorizontal()){
            Vec3i p = pos.plus(side.increment);
            if (!map.hasPosition(p)) continue;
            DwarfCube nb = map.cubeAt(p);
            if (nb == null) dirList.add(side);
            else if (nb.type == STAIRS || nb.type == LADDER || nb.type == COLLECTOR)
                addVert = false;
        }
        if (addVert){
            if (dirIsClear(map, pos, DwarfDirection.POS_Y))
                dirList.add(DwarfDirection.POS_Y);
            if (dirIsClear(map, pos, DwarfDirection.NEG_Y))
                dirList.add(DwarfDirection.NEG_Y);
        }

        return dirList.toArray(new DwarfDirection[0]);
    }


    // Берёт n направлений из src
    private static DwarfDirection[] getRandomDirs(DwarfSet set, DwarfDirection[]src, int n){
        var dst = new DwarfDirection[n];
        for (int i = 0, rest = n; i < n; i++) {
            double rand = set.random();
            int pos = (int) Math.floor((src.length)*rand);
            if (rand == 1) --pos;
            dst[i] = src[pos];
            --rest;
            if (rest == 0) return dst;
            src = removeOne(src, pos);
        }
        return dst;
    }

    // Удаляет из массива направлений элемент n
    private static DwarfDirection[] removeOne(DwarfDirection[]src, int n){
        DwarfDirection[]dst = new DwarfDirection[src.length-1];
        for (int i = 0, j = 0; i < src.length; i++) {
            if (i != n){
                dst[j] = src[i];
                j++;
            }
        }
        return dst;
    }

    private static DwarfWay[] asWays(Vec3i pos, DwarfDirection[]dirs){
        DwarfWay[]ways = new DwarfWay[dirs.length];
        for (int i = 0; i < ways.length; i++)
            ways[i] = new DwarfWay(pos, dirs[i]);
        return ways;
    }


}
