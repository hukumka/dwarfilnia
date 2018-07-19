package nktl.dwarf;

import nktl.math.RangeInt;
import nktl.math.geom.Vec3i;
import nktl.math.graph.Graph;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

import static nktl.dwarf.DwarfCube.CubeType;
import static nktl.dwarf.DwarfCube.Feature;

public class DwarfMap {


    /*
        ПЕРЕМЕННЫЕ
     */
    private DwarfCube[] map;
    private Graph<DwarfCube> graph;

    private int dx, dy, dz;
    private int layerSize;

    private RangeInt
            rangeX = new RangeInt(),
            rangeY = new RangeInt(),
            rangeZ = new RangeInt();

    // Специальная фигня для последних граничных кубов
    private Vec3i[]edges = null;
    private static final int
            x_min = 0, x_max = 1,
            y_min = 2, y_max = 3,
            z_min = 4, z_max = 5;


    /*
        КОНСТРУКТОРЫ
     */
    DwarfMap(Vec3i dim){
        this.dx = dim.x;
        this.dy = dim.y;
        this.dz = dim.z;

        rangeX.set(0, dim.x-1);
        rangeY.set(0, dim.y-1);
        rangeZ.set(0, dim.z-1);

        layerSize = dx * dy;

        map = new DwarfCube[dim.x*dim.y*dim.z];
        graph = new Graph<>();
    }

    /*
        PUBLIC
     */
    public Graph<DwarfCube> graph(){
        return graph;
    }

    public DwarfCube cubeAt(Vec3i pos) {
        return map[arrayPosition(pos)];
    }

    public Vec3i randomPos(DwarfSet set) {
        return new Vec3i(
                RangeInt.from01(set.random(), rangeX),
                RangeInt.from01(set.random(), rangeY),
                RangeInt.from01(set.random(), rangeZ)
        );
    }

    public DwarfCube addNodeCubeAt(Vec3i position) {
        if (!hasPosition(position)) return null;
        if (cubeAt(position) != null) return null;
        DwarfCube cube = addCubeAt(position);
        var node = graph.newNode(cube);
        cube.setNode(node);
        expand(position);
        return cube;
    }

    public DwarfCube addCubeAt(Vec3i position){
        return addCubeAt(position, new DwarfCube());
    }

    public DwarfCube addCubeAt(Vec3i position, DwarfCube cube) {
        map[arrayPosition(position)] = cube;
        cube.position.copy(position);
        return cube;
    }

    public DwarfCube addCubeAt(Vec3i position, Graph<DwarfCube>.Node node) {
        DwarfCube cube = addCubeAt(position);
        cube.setNode(node);
        return cube;
    }

    public boolean hasPosition(Vec3i pos) {
        boolean has = rangeX.has(pos.x)
                && rangeY.has(pos.y)
                && rangeZ.has(pos.z);
        if (!has) return false;
        DwarfCube cube = cubeAt(pos);
        if (cube != null && cube.type==CubeType.PLUG)
            has = false;
        return has;
    }

    public LinkedList<DwarfCube> getCubes(){
        LinkedList<DwarfCube> cubes = new LinkedList<>();
        for (DwarfCube cube : map){
            if (cube != null && cube.type != CubeType.PLUG){
                fixWaysAndType(cube);
                cubes.add(cube);
            }
        }
        return cubes;
    }

    public DwarfCube getNeighbour(Vec3i pos, DwarfDirection dir){
        Vec3i p = pos.plus(dir.increment);
        if (!hasPosition(p)) return null;
        return cubeAt(p);
    }

    /*
        PRIVATE
     */

    private void fixWaysAndType(DwarfCube cube){
        switch (cube.type){
            case UNKNOWN:
                cube.type = CubeType.TUNNEL;
            case TUNNEL:
                for (DwarfDirection dir : DwarfDirection.getHorizontal()){
                    DwarfCube nbr = getNeighbour(cube.position, dir);
                    if (nbr == null) continue;
                    if (!nbr.hasBit(Feature.CAGE, dir.getBack().bit) &&
                            !nbr.hasBit(Feature.DOOR, dir.getBack().bit) &&
                            !cube.hasBit(Feature.CAGE, dir.bit) &&
                            !cube.hasBit(Feature.DOOR, dir.bit)
                            ) cube.addBit(Feature.WAY, dir.bit);
                }
                break;
            case LADDER:
                for (DwarfDirection dir : DwarfDirection.getHorizontal()){
                    DwarfCube nbr = getNeighbour(cube.position, dir);
                    if (nbr == null) continue;
                    if (!nbr.hasBit(Feature.CAGE, dir.getBack().bit) &&
                            !nbr.hasBit(Feature.DOOR, dir.getBack().bit)
                            ) cube.addBit(Feature.WAY, dir.bit);
                }
                DwarfCube up = getNeighbour(cube.position, DwarfDirection.POS_Y);
                DwarfCube down = getNeighbour(cube.position, DwarfDirection.NEG_Y);
                if (up != null)
                    if (up.type == CubeType.LADDER)
                        cube.addBit(Feature.WAY, DwarfDirection.POS_Y.bit);
                if (down!= null)
                    if (down.type == CubeType.LADDER)
                        cube.addBit(Feature.WAY, DwarfDirection.NEG_Y.bit);
                break;
            case COLLECTOR:
                if (cube.features().containsKey(Feature.WATER))
                    cube.addBit(Feature.WAY, DwarfDirection.POS_Y.bit);
                else {
                    for (DwarfDirection dir : DwarfDirection.getHorizontal()){
                        DwarfCube nbr = getNeighbour(cube.position, dir);
                        if (nbr == null) continue;
                        if (!nbr.hasBit(Feature.CAGE, dir.getBack().bit) &&
                                !nbr.hasBit(Feature.DOOR, dir.getBack().bit)
                                ) cube.addBit(Feature.WAY, dir.bit);
                    }
                    up = getNeighbour(cube.position, DwarfDirection.POS_Y);
                    down = getNeighbour(cube.position, DwarfDirection.NEG_Y);
                    if (up != null)
                        if (up.type == CubeType.COLLECTOR)
                            cube.addBit(Feature.WAY, DwarfDirection.POS_Y.bit);
                    if (down!= null)
                        if (down.type == CubeType.COLLECTOR)
                            cube.addBit(Feature.WAY, DwarfDirection.NEG_Y.bit);
                }
                break;
        }
    }



    private Vec3i mapPosition(int array_position){
        int z = array_position/layerSize;
        array_position -= z*layerSize;
        int y = array_position/dx;
        int x = array_position - y*dx;
        return new Vec3i(x, y, z);
    }

    private int arrayPosition(Vec3i map_position){
        return map_position.x + map_position.y*dx + map_position.z*layerSize;
    }

    private void expand(Vec3i pos){
        if (edges == null) {
            edges = new Vec3i[6];
            for (int i = 0; i < edges.length; i++)
                edges[i] = new Vec3i(pos);
        } else {
            if (pos.x < edges[x_min].x) edges[x_min].copy(pos);
            if (pos.y < edges[y_min].y) edges[y_min].copy(pos);
            if (pos.z < edges[z_min].z) edges[z_min].copy(pos);
            if (pos.x < edges[x_max].x) edges[x_max].copy(pos);
            if (pos.y < edges[y_max].y) edges[y_max].copy(pos);
            if (pos.z < edges[z_max].z) edges[z_max].copy(pos);
        }
    }

    Vec3i[] getEdges() {
        HashSet<Vec3i> edgeSet = new HashSet<>();
        Collections.addAll(edgeSet, edges);
        return edgeSet.toArray(new Vec3i[0]);
    }

    /*
        STATIC
     */



}
