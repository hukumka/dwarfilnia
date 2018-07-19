package nktl.dwarf;

import nktl.math.geom.Vec3i;
import nktl.math.graph.Graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

public class DwarfCube {

    public static final int
            TYPE_PLUG = -1,
            TYPE_UNKNOWN = 0,
            TYPE_TUNNEL = 1,
            TYPE_COLLECTOR = 2,
            TYPE_LADDER = 3,
            TYPE_STAIRS = 4,
            TYPE_ROOT_CUBE = 5;

    public enum Feature {
        SEWER, CAGE, DOOR, WAY, WATER, DESTRUCTION
    }

    public enum CubeType {
        PLUG, UNKNOWN, TUNNEL, COLLECTOR, LADDER, STAIRS, ROOT_CUBE;
    }

    /*
        ПЕРЕМЕННЫЕ
     */
    Graph<DwarfCube>.Node node = null;
    Vec3i position = new Vec3i();
    CubeType type = CubeType.UNKNOWN;
    HashMap<Feature, Integer> features = new HashMap<>();



    /*
        PUBLIC
     */
    public void setNode(Graph<DwarfCube>.Node node){
        this.node = node;
    }

    public boolean hasSewer(){
        return features.containsKey(Feature.SEWER);
    }
    public boolean isFat() {
        return this.type == CubeType.LADDER;
    }

    public void addBit(Feature feature, int bit){
        if (!features.containsKey(feature))
            features.put(feature, bit);
        else
            features.put(feature, features.get(feature) | bit);
    }

    public boolean hasBit(Feature feature, int bit){
        if (!features.containsKey(feature)) return false;
        return (features.get(feature) & bit) == bit;
    }

    public HashMap<Feature, Integer> features() { return features; }
    public Vec3i position(){ return position; }
    public CubeType type(){ return type; }



    /*
        STATIC
     */

    public static HashMap<CubeType, LinkedList<DwarfCube>> separate(Collection<DwarfCube> cubes){
        var map = new HashMap<CubeType, LinkedList<DwarfCube>>();
        for (DwarfCube cube : cubes){
            if (!map.containsKey(cube.type))
                map.put(cube.type, new LinkedList<>());
            map.get(cube.type).add(cube);
        }
        return map;
    }

}
