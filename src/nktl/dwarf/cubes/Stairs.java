package nktl.dwarf.cubes;

import nktl.math.geom.Direction;
import nktl.math.geom.Vec3i;

public class Stairs extends BaseCube{
    private Direction direction;
    private boolean isBrokenAbove;
    private boolean isBrokenBelow;

    Stairs(Vec3i pos){
        super(pos);
        this.direction = Direction.NORTH;
    }
    Stairs(Vec3i pos, Direction direction){
        super(pos);
        this.direction = direction;
    }

    public Direction direction(){
        return this.direction;
    }

    @Override
    public boolean is_fat(){
        return true;
    }

    public boolean isBrokenAbove() {
        return isBrokenAbove;
    }

    public void setBrokenAbove(boolean brokenAbove) {
        isBrokenAbove = brokenAbove;
    }

    public boolean isBrokenBelow() {
        return isBrokenBelow;
    }

    public void setBrokenBelow(boolean brokenBelow) {
        isBrokenBelow = brokenBelow;
    }
}
