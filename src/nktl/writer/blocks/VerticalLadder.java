package nktl.writer.blocks;

import nktl.generator.DwarfCube;
import nktl.math.geom.Direction;
import nktl.math.geom.Vec3i;
import nktl.server.MinecraftRMIProcess;
import nktl.server.commands.Fill;

import java.io.IOException;

public class VerticalLadder implements DwarfBlock {
    boolean top = false;
    boolean bottom = false;
    Direction direction = Direction.NORTH;
    int ways = 0xf;

    public VerticalLadder setDirection(Direction direction){
        this.direction = direction;
        return this;
    }

    public VerticalLadder setWays(int ways){
        this.ways = ways;
        return this;
    }

    @Override
    public void placeAt(MinecraftRMIProcess process, Vec3i position) throws IOException {
        // fill floor and ceiling
        new Fill(
                position,
                position.plus(4, 4, 4),
                "minecraft:stonebrick"
        ).runIn(process);
        // make space in the middle
        new Fill(
                position.plus(-1, 1, -1),
                position.plus(5, 3, 5),
                "minecraft:air"
        ).runIn(process);
        // make column
        new Fill(
                position.plus(1, 1, 1),
                position.plus(3, 3, 3),
                "minecraft:cobblestone"
        ).runIn(process);
        new Fill(position.plus(1, 0, 1), position.plus(1, 4, 1), "minecraft:stone")
               .dataValue(6)
               .runIn(process);
        new Fill(position.plus(3, 0, 1), position.plus(3, 4, 1), "minecraft:stone")
               .dataValue(6)
               .runIn(process);
        new Fill(position.plus(3, 0, 3), position.plus(3, 4, 3), "minecraft:stone")
               .dataValue(6)
               .runIn(process);
        new Fill(position.plus(1, 0, 3), position.plus(1, 4, 3), "minecraft:stone")
               .dataValue(6)
               .runIn(process);
        // make stairs
        new Fill(position.plus(-1, 1, -1), position.plus(4, 1, -1), "minecraft:stone_brick_stairs")
               .dataValue(3)
               .runIn(process);
        new Fill(position.plus(5, 1, -1), position.plus(5, 1, 4), "minecraft:stone_brick_stairs")
                .dataValue(0)
                .runIn(process);
        new Fill(position.plus(5, 1, 5), position.plus(0, 1, 5), "minecraft:stone_brick_stairs")
                .dataValue(2)
                .runIn(process);
        new Fill(position.plus(-1, 1, 5), position.plus(-1, 1, -1), "minecraft:stone_brick_stairs")
                .dataValue(1)
                .runIn(process);
        new Fill(position.plus(-1, 3, -1), position.plus(4, 3, -1), "minecraft:stone_brick_stairs")
                .dataValue(7)
                .runIn(process);
        new Fill(position.plus(5, 3, -1), position.plus(5, 3, 4), "minecraft:stone_brick_stairs")
                .dataValue(4)
                .runIn(process);
        new Fill(position.plus(5, 3, 5), position.plus(0, 3, 5), "minecraft:stone_brick_stairs")
                .dataValue(6)
                .runIn(process);
        new Fill(position.plus(-1, 3, 5), position.plus(-1, 3, -1), "minecraft:stone_brick_stairs")
                .dataValue(5)
                .runIn(process);

        if((ways&DwarfCube.DIRECTION_WEST_BIT) > 0){
            new Fill(position.plus(-1, 1, 1), position.plus(-1, 3, 3), "minecraft:air")
                    .runIn(process);
        }
        if((ways&DwarfCube.DIRECTION_EAST_BIT) > 0){
            new Fill(position.plus(5, 1, 1), position.plus(5, 3, 3), "minecraft:air")
                    .runIn(process);
        }
        if((ways&DwarfCube.DIRECTION_NORTH_BIT) > 0){
            new Fill(position.plus(1, 1, 5), position.plus(3, 3, 5), "minecraft:air")
                    .runIn(process);
        }
        if((ways&DwarfCube.DIRECTION_SOUTH_BIT) > 0){
            new Fill(position.plus(1, 1, -1), position.plus(3, 3, -1), "minecraft:air")
                    .runIn(process);
        }


        // make entrance
        Vec3i pos = position.plus(2, 1, 2).plus(Vec3i.fromDirection(direction));
        new Fill(
                pos,
                pos.plus(0, 2, 0),
                "minecraft:air"
        ).runIn(process);
        // make ladder
        int data = 0;
        switch (direction){
            case NORTH:
                data = 2;
                break;
            case SOUTH:
                data = 3;
                break;
            case WEST:
                data = 4;
                break;
            case EAST:
                data = 5;
                break;
        }

        int start = bottom?1:0;
        int end = top?3:4;

        new Fill(
                position.plus(2, start, 2),
                position.plus(2, end, 2),
                "minecraft:ladder"
        )
                .dataValue(data)
                .runIn(process);

    }
}
