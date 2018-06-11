package nktl.math.geom;

import nktl.generator.Generator;

public enum Direction {
    NORTH, SOUTH, EAST, WEST;

    public Direction getLeft() {
        switch (this) {
            case NORTH:
                return WEST;
            case WEST:
                return SOUTH;
            case SOUTH:
                return EAST;
            default:
                return NORTH;
        }
    }

    public Direction getRight() {
        switch (this) {
            case NORTH:
                return EAST;
            case EAST:
                return SOUTH;
            case SOUTH:
                return WEST;
            default:
                return NORTH;
        }
    }

    public int rotationCount(){
        switch (this){
            case EAST:
                return 0;
            case NORTH:
                return 1;
            case WEST:
                return 2;
            case SOUTH:
                return 3;
            default:
                return 0;
        }
    }
}
