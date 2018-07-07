package nktl.dwarf.cubes;

import nktl.math.geom.Direction;
import nktl.math.geom.Vec3i;

public class DiagonalLadder extends BaseCube{
    private Direction direction;

    DiagonalLadder(Vec3i pos){
        super(pos);
        this.direction = Direction.NORTH;
    }
    DiagonalLadder(Vec3i pos, Direction direction){
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
}
