package nktl.dwarf;


import nktl.math.RangeInt;
import nktl.math.geom.Vec3i;
import nktl.math.graph.Graph;

import java.util.HashSet;

import static nktl.dwarf.DwarfCube.TYPE_EXCLUDED;

public class DwarfMap {

    /*
        Переменные
     */
    private DwarfCube[] map;
    private HashSet<DwarfCube> cubes;
    private Graph<DwarfCube> graph;
    private RangeInt
            rangeX = new RangeInt(),
            rangeY = new RangeInt(),
            rangeZ = new RangeInt();
    private int dx, dy, dz;
    private int layerSize;
    private DwarfCube
            edgePosX = null, edgePosY = null, edgePosZ = null,
            edgeNegX = null, edgeNegY = null, edgeNegZ = null;

    /*
        Конструкторы
     */

    public DwarfMap(Vec3i dim){
        this.dx = dim.x;
        this.dy = dim.y;
        this.dz = dim.z;

        rangeX.set(0, dim.x-1);
        rangeY.set(0, dim.y-1);
        rangeZ.set(0, dim.z-1);

        layerSize = dx * dy;

        map = new DwarfCube[dim.x*dim.y*dim.z];
        cubes = new HashSet<>();
        graph = new Graph<>();
    }

    /*
        Public
     */

    public HashSet<DwarfCube> cubes(){
        return cubes;
    }

    public Graph<DwarfCube> graph(){
        return graph;
    }

    public boolean hasPosition(Vec3i pos) {
        if (!rangeX.has(pos.x) || !rangeY.has(pos.y) || !rangeZ.has(pos.z)) return false;
        if (!hasCubeAt(pos)) return true;
        return map[arrayPos(pos)].type != TYPE_EXCLUDED;
    }

    public boolean hasCubeAt(Vec3i pos){
        return map[arrayPos(pos)] != null;
    }

    public Vec3i getRandomPosition(DwarfSet set){
        return new Vec3i(
                RangeInt.from01(set.random(), rangeX),
                RangeInt.from01(set.random(), rangeY),
                RangeInt.from01(set.random(), rangeZ)
        );
    }

    public DwarfCube getCubeAt(int array_pos){
        return map[array_pos];
    }

    public int getMapArraySize(){
        return map.length;
    }

    public Vec3i getMapSize(){
        return new Vec3i(dx, dy, dz);
    }

    public DwarfCube getCubeAt(Vec3i pos){
        return map[arrayPos(pos)];
    }

    public DwarfCube addCubeAtSafe(Vec3i pos, int type) {
        return addCubeAtSafe(pos, type, null);
    }

    public DwarfCube addCubeAtSafe(Vec3i pos, int type, Graph<DwarfCube>.Node node) {
        if (!hasPosition(pos)) return null;
        return addCubeAt(pos, type, node);
    }

    public DwarfCube addCubeAt(Vec3i pos, int type){
        return addCubeAt(pos, type, null);
    }

    public DwarfCube addCubeAt(Vec3i pos, int type, Graph<DwarfCube>.Node node) {
        DwarfCube cube;
        cube = map[arrayPos(pos)] = new DwarfCube(pos, type);
        if (type != TYPE_EXCLUDED) {
            cube.setNode(node);
            cubes.add(cube);
        }
        return cube;
    }



    /*
        Private
     */
    private int arrayPos(Vec3i pos){
        return pos.x + pos.y*dx + pos.z*layerSize;
    }

    private Vec3i mapPos(int mapPos) {
        int z = mapPos/layerSize;
        mapPos -= z*layerSize;
        int y = mapPos/dx;
        int x = mapPos - y*dx;
        return new Vec3i(x, y, z);
    }


    public DwarfWay[] getEdgeWays() {
        return new DwarfWay[0];
        // TODO: 07.07.2018
        /*
        return new DwarfWay[]{
                new DwarfWay(edgePosX.node, edgePosX.position, 0),
                new DwarfWay(edgePosY.node, edgePosY.position, 0),
                new DwarfWay(edgePosZ.node, edgePosZ.position, 0),
                new DwarfWay(edgeNegX.node, edgeNegX.position, 0),
                new DwarfWay(edgeNegY.node, edgeNegY.position, 0),
                new DwarfWay(edgeNegZ.node, edgeNegZ.position, 0)
        };*/
    }
}
