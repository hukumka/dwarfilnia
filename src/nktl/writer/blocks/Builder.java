package nktl.writer.blocks;

import nktl.math.geom.Direction;
import nktl.math.geom.Vec3i;
import nktl.server.commands.BlockData;
import nktl.server.commands.Fill;
import nktl.server.commands.states.Facing;
import nktl.server.commands.states.Half;

public class Builder {
    public static Fill createStairs(Vec3i from, Vec3i to, Direction direction, boolean upsideDown){
        BlockData data = new BlockData("minecraft:stone_brick_stairs")
                .addParam(Facing.ofDirection(direction))
                .addParam(upsideDown? Half.top: Half.bottom);

        return new Fill(from, to, data);
    }

    public static Fill createStairsSingle(Vec3i pos, Direction direction, boolean upsideDown){
        return createStairs(pos, pos, direction, upsideDown);
    }
}
