package nktl.math.geom;

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

    public Direction getBack() {
        switch (this) {
            case NORTH:
                return SOUTH;
            case EAST:
                return WEST;
            case SOUTH:
                return NORTH;
            default:
                return EAST;
        }
    }

    public boolean isParallelTo(Direction dir){
        return dir == this || dir == this.getBack();
    }
}
