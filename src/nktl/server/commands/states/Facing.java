package nktl.server.commands.states;

import nktl.math.geom.Direction;
import nktl.server.commands.BlockParam;

public enum Facing implements BlockParam {
    north, south, east, west;

    @Override
    public Facing rotate90Y() {
        switch (this) {
            case west: return south;
            case south: return east;
            case east: return north;
            case north: default: return west;
        }
    }

    @Override
    public String getParamString() {
        return "facing="+this;
    }

    public static Facing ofDirection(Direction dir) {
        switch (dir) {
            case EAST: return east;
            case SOUTH: return south;
            case WEST: return west;
            case NORTH: default: return north;
        }
    }
}
