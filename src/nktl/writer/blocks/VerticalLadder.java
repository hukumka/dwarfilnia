package nktl.writer.blocks;

import nktl.dwarf.DwarfCube;
import nktl.dwarf.DwarfDirection;
import nktl.math.geom.Direction;
import nktl.math.geom.Vec3i;
import nktl.server.MinecraftRMIProcess;
import nktl.server.commands.BlockData;
import nktl.server.commands.Fill;
import nktl.server.commands.states.Facing;
import nktl.server.commands.states.Half;

import java.io.IOException;

public class VerticalLadder implements DwarfBlock {
    boolean top = false;
    boolean bottom = false;
    Direction direction = Direction.NORTH;
    int ways = 0xf;


    static public VerticalLadder from_dwarf_cube(DwarfCube cube){
        VerticalLadder res = new VerticalLadder();
        if(cube.features().containsKey(DwarfCube.Feature.WAY)){
            res.setWays(cube.features().get(DwarfCube.Feature.WAY));
        }
        res.top = (res.ways&DwarfDirection.BIT_POS_Y) == 0;
        res.bottom = (res.ways&DwarfDirection.BIT_NEG_Y) == 0;
        return res;
    }

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
                new BlockData("minecraft:stone_bricks")
        ).runIn(process);
        // make space in the middle
        new Fill(
                position.plus(-1, 1, -1),
                position.plus(5, 3, 5),
                new BlockData("minecraft:air")
        ).runIn(process);
        // make column
        new Fill(
                position.plus(1, 1, 1),
                position.plus(3, 3, 3),
                new BlockData("minecraft:cobblestone")
        ).runIn(process);
        BlockData andesite = new BlockData("minecraft:polished_andesite");
        new Fill(position.plus(1, 0, 1), position.plus(1, 4, 1), andesite)
               .runIn(process);
        new Fill(position.plus(3, 0, 1), position.plus(3, 4, 1), andesite)
               .runIn(process);
        new Fill(position.plus(3, 0, 3), position.plus(3, 4, 3), andesite)
               .runIn(process);
        new Fill(position.plus(1, 0, 3), position.plus(1, 4, 3), andesite)
               .runIn(process);
        // make stairs
        new Fill(position.plus(-1, 1, -1), position.plus(4, 1, -1), new BlockData("minecraft:stone_brick_stairs").addParam(Facing.north))
               .runIn(process);
        new Fill(position.plus(5, 1, -1), position.plus(5, 1, 4), new BlockData("minecraft:stone_brick_stairs").addParam(Facing.east))
                .runIn(process);
        new Fill(position.plus(5, 1, 5), position.plus(0, 1, 5), new BlockData("minecraft:stone_brick_stairs").addParam(Facing.south))
                .runIn(process);
        new Fill(position.plus(-1, 1, 5), position.plus(-1, 1, -1), new BlockData("minecraft:stone_brick_stairs").addParam(Facing.west))
                .runIn(process);
        new Fill(position.plus(-1, 3, -1), position.plus(4, 3, -1), new BlockData("minecraft:stone_brick_stairs").addParam(Facing.north).addParam(Half.top))
                .runIn(process);
        new Fill(position.plus(5, 3, -1), position.plus(5, 3, 4), new BlockData("minecraft:stone_brick_stairs").addParam(Facing.east).addParam(Half.top))
                .runIn(process);
        new Fill(position.plus(5, 3, 5), position.plus(0, 3, 5), new BlockData("minecraft:stone_brick_stairs").addParam(Facing.south).addParam(Half.top))
                .runIn(process);
        new Fill(position.plus(-1, 3, 5), position.plus(-1, 3, -1), new BlockData("minecraft:stone_brick_stairs").addParam(Facing.west).addParam(Half.top))
                .runIn(process);

        if((ways&DwarfDirection.BIT_NEG_X) > 0){ // WEST
            new Fill(position.plus(-1, 1, 1), position.plus(-1, 3, 3), new BlockData("minecraft:air"))
                    .runIn(process);
        }
        if((ways&DwarfDirection.BIT_POS_X) > 0){ // EAST
            new Fill(position.plus(5, 1, 1), position.plus(5, 3, 3), new BlockData("minecraft:air"))
                    .runIn(process);
        }
        if((ways&DwarfDirection.BIT_POS_Z) > 0){ // SOUTH
            new Fill(position.plus(1, 1, 5), position.plus(3, 3, 5), new BlockData("minecraft:air"))
                    .runIn(process);
        }
        if((ways&DwarfDirection.BIT_NEG_Z) > 0){ // NORTH
            new Fill(position.plus(1, 1, -1), position.plus(3, 3, -1), new BlockData("minecraft:air"))
                    .runIn(process);
        }


        // make entrance
        Vec3i pos = position.plus(2, 1, 2).plus(Vec3i.fromDirection(direction));
        new Fill(
                pos,
                pos.plus(0, 2, 0),
                new BlockData("minecraft:air")
        ).runIn(process);
        int start = bottom?1:0;
        int end = top?3:4;

        new Fill(
                position.plus(2, start, 2),
                position.plus(2, end, 2),
                new BlockData("minecraft:ladder")
                    .addParam(Facing.ofDirection(direction))
        )
                .runIn(process);

    }
}
