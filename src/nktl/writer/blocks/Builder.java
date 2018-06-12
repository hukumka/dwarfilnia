package nktl.writer.blocks;

import nktl.math.geom.Direction;
import nktl.math.geom.Vec3i;
import nktl.server.commands.Fill;

public class Builder {
    public static Fill createStairs(Vec3i from, Vec3i to, Direction direction, boolean upsideDown){
        int data = 0;
        switch (direction){
            case EAST:
                data = 0;
                break;
            case WEST:
                data = 1;
                break;
            case SOUTH:
                data = 2;
                break;
            case NORTH:
                data = 3;
                break;
        }
        if(upsideDown){
            data += 4;
        }
        return new Fill(from, to, "minecraft:stone_brick_stairs")
                .dataValue(data);
    }

    public static Fill createStairsSingle(Vec3i pos, Direction direction, boolean upsideDown){
        return createStairs(pos, pos, direction, upsideDown);
    }
}
