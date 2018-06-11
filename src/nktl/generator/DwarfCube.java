package nktl.generator;

import nktl.math.geom.Direction;
import nktl.math.geom.Vec3i;

public class DwarfCube {

    public static final int
            TYPE_EXCLUDED = -1,
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

            DATA_NORTH_BIT  = 0b10000,
            DATA_SOUTH_BIT  = 0b100000,
            DATA_EAST_BIT   = 0b1000000,
            DATA_WEST_BIT   = 0b10000000;

    public boolean typeIs(int type){
        return this.type == type;
    }

    public boolean directionHas(int bit){
        return (this.direction & bit) > 0;
    }

    public void addDirBit(int bit){
        this.direction |= bit;
    }

    public void removeDirBit(int bit){
        this.direction &= (~bit);
    }

    int type = TYPE_TUNNEL;
    int direction = 0;
    Vec3i position = new Vec3i();

    public DwarfCube(){}

    public DwarfCube(Vec3i position){
        this.position.copy(position);
    }

    public int getType() {
        return type;
    }

    public Vec3i getPosition() {
        return position;
    }

    public int getDirection() {
        return direction;
    }

    public void setType(int type) {
        this.type = type;
    }

    public DwarfCube copy(){
        DwarfCube dc = new DwarfCube();
        dc.position.copy(this.position);
        dc.type = this.type;
        dc.direction = this.direction;
        return dc;
    }

    public Direction enumDirection(){
        boolean north = directionHas(DIRECTION_NORTH_BIT),
                south = directionHas(DIRECTION_SOUTH_BIT),
                east = directionHas(DIRECTION_EAST_BIT),
                west = directionHas(DIRECTION_WEST_BIT);
        boolean northAndSouth = north && south;
        boolean eastAndWest = east && west;
        if (northAndSouth){
            if (eastAndWest) return Direction.NORTH;
            if (east) return Direction.EAST;
            if (west) return Direction.WEST;
            return Direction.NORTH;
        }
        if (north) {
            if (west && !east) return Direction.WEST;
            return Direction.NORTH;
        }
        if (south) {
            if (east && !west) return Direction.EAST;
            return Direction.SOUTH;
        }
        if (east) return Direction.EAST;
        if (west) return Direction.WEST;
        return Direction.NORTH;
    }

    public static int dirEnumToBit(Direction direction){
        switch (direction) {
            case NORTH: return DIRECTION_NORTH_BIT;
            case SOUTH: return DIRECTION_SOUTH_BIT;
            case EAST: return DIRECTION_EAST_BIT;
            default: return DIRECTION_WEST_BIT;
        }
    }

    public static Direction dirBitToEnum(int bit){
        if ((bit & DIRECTION_NORTH_BIT) > 0) return Direction.NORTH;
        if ((bit & DIRECTION_EAST_BIT) > 0) return Direction.EAST;
        if ((bit & DIRECTION_SOUTH_BIT) > 0) return Direction.SOUTH;
        return Direction.WEST;
    }
}
