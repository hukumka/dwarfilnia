package nktl.generator;

import nktl.math.geom.Vec3i;

public class DwarfCube {

    public static final int
            TYPE_TUNNEL = 0,
            TYPE_COLLECTOR = 1,
            TYPE_VERTICAL_LADDER = 2,
            TYPE_DIAGONAL_LADDER = 3;

    // Биты
    public static final int
            DIRECTION_NORTH_BIT = 0b1,
            DIRECTION_SOUTH_BIT = 0b10,
            DIRECTION_EAST_BIT  = 0b100,
            DIRECTION_WEST_BIT  = 0b1000,

            EXTRA_DIRECTION_NORTH_BIT  = 0b10000,
            EXTRA_DIRECTION_SOUTH_BIT  = 0b100000,
            EXTRA_DIRECTION_EAST_BIT   = 0b1000000,
            EXTRA_DIRECTION_WEST_BIT   = 0b10000000;

    public boolean typeIs(int type){
        return this.type == type;
    }

    public boolean directionHas(int bit){
        return (this.direction & bit) > 0;
    }

    public void addDirBit(int bit){
        this.direction &= bit;
    }

    public void removeDirBit(int bit){
        this.direction &= (~bit);
    }

    int type = TYPE_TUNNEL;
    int direction = 0;
    Vec3i position = new Vec3i();

    DwarfCube(){}

    DwarfCube(Vec3i position){
        this.position.copy(position);
    }

    public int getType() {
        return type;
    }

    public Vec3i getPosition() {
        return position;
    }
}
