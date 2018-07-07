package nktl.dwarf.cubes;

import nktl.math.geom.Direction;
import nktl.math.geom.Vec3i;

public class Ladder extends BaseCube{
    private Direction direction;

    Ladder(Vec3i pos){
        super(pos);
        this.direction = Direction.NORTH;
    }
    Ladder(Vec3i pos, Direction direction){
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
