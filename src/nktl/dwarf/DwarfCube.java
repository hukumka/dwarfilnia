package nktl.dwarf;


import nktl.math.geom.Vec3i;
import nktl.math.graph.Graph;

import java.util.HashSet;

public class DwarfCube implements Attractor {
    /*
        Константы
     */
    public static final int
            TYPE_EXCLUDED = -1,
            TYPE_TUNNEL = 0,
            TYPE_COLLECTOR = 1,
            TYPE_VERTICAL_LADDER = 2,
            TYPE_DIAGONAL_LADDER = 3,
            TYPE_ROOT_CUBE = 4;

    public enum Feature {
        SEWER, CAGE, DOOR, ENTRANCE, WAY;
        byte data;
        public byte getData(){
            return data;
        }
        public boolean hasBit(int bit){
            return (data & bit) == bit;
        }
    }

    public static final int
            DIRECTION_POS_X = 0b1,
            DIRECTION_POS_Z = 0b10,
            DIRECTION_NEG_X = 0b100,
            DIRECTION_NEG_Z = 0b1000,
            DIRECTION_POS_Y = 0b10000,
            DIRECTION_NEG_Y = 0b100000;

    /*
        Переменные
     */
    int type;
    HashSet<Feature> features = new HashSet<>();
    Vec3i position = new Vec3i();
    Graph<DwarfCube>.Node node = null;


    /*
        PUBLIC
     */
    public DwarfCube(Vec3i pos, int type){
        this.position.copy(pos);
        this.type = type;
    }

    public void setNode(Graph<DwarfCube>.Node node) {
        this.node = node;
    }

    public Vec3i position(){
        return position;
    }

    public double distanceTo(Vec3i pos){
        return new Vec3i(
                position.x - pos.x,
                position.y - pos.y,
                position.z - pos.z).length();
    }

    public int type(){
        return type;
    }

    public boolean isFat(){
        return type == TYPE_VERTICAL_LADDER;
    }


}
