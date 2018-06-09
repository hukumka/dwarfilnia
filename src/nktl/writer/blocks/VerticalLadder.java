package nktl.writer.blocks;

import nktl.math.geom.Direction;
import nktl.math.geom.Vec3i;
import nktl.server.MinecraftRMIProcess;
import nktl.server.commands.Fill;

import java.io.IOException;

public class VerticalLadder implements DwarfBlock {
    boolean top = false;
    boolean bottom = false;
    Direction direction = Direction.NORTH;

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
                 position.plus(0, 1, 0),
                 position.plus(4, 3, 4),
                 "minecraft:air"
         ).runIn(process);
         // make column
         new Fill(
                 position.plus(1, 0, 1),
                 position.plus(3, 4, 3),
                 "minecraft:stonebrick"
         ).runIn(process);

         // make entrance
         Vec3i pos = position.plus(2, 1, 2).plus(Vec3i.fromDirection(direction));
         new Fill(
                 pos,
                 pos.plus(0, 2, 0),
                 "minecraft:air"
         ).runIn(process);
         // make ladder
        String data = null;
        switch (direction){
            case NORTH:
                data = "2";
                break;
            case SOUTH:
                data = "3";
                break;
            case WEST:
                data = "4";
                break;
            case EAST:
                data = "5";
                break;
        }
        new Fill(
                position.plus(2, 0, 2),
                position.plus(2, 4, 2),
                "minecraft:ladder"
        )
                .dataValue(data)
                .runIn(process);

    }
}
