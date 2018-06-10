package nktl.writer.blocks;

import nktl.math.geom.Direction;
import nktl.math.geom.Vec3i;
import nktl.server.MinecraftRMIProcess;
import nktl.server.commands.Fill;

import java.io.IOException;

public class SewerStraitCorridor implements DwarfBlock {
    boolean isNorthSouth;

    public SewerStraitCorridor(boolean isNorthSouth){
        this.isNorthSouth = isNorthSouth;
    }

    @Override
    public void placeAt(MinecraftRMIProcess process, Vec3i position) throws IOException {
        // create space
        new Fill(position, position.plus(4, 4, 4), "minecraft:air")
                .runIn(process);

        // create floor
        new Fill(position, position.plus(4, 0, 4), "minecraft:stonebrick")
                .runIn(process);
        // create ceiling
        new Fill(position.plus(0, 4, 0), position.plus(4, 4, 4), "minecraft:stonebrick")
                .runIn(process);

        if(isNorthSouth) {
            // create water line
            Vec3i start = position.plus(Vec3i.fromDirection(Direction.EAST).mult(2));
            Vec3i end = start.plus(Vec3i.fromDirection(Direction.SOUTH).mult(4));
            new Fill(start, end, "minecraft:water")
                    .runIn(process);

            // create stairs edges
            start = position.plus(0, 1, 0);
            new Fill(start, start.plus(Vec3i.fromDirection(Direction.SOUTH).mult(4)), "minecraft:stone_brick_stairs")
                    .dataValue("1") // face west
                    .runIn(process);
            start = start.plus(0, 2, 0);
            new Fill(start, start.plus(Vec3i.fromDirection(Direction.SOUTH).mult(4)), "minecraft:stone_brick_stairs")
                    .dataValue("5") // face west upside down
                    .runIn(process);
            start = position.plus(Vec3i.fromDirection(Direction.EAST).mult(4)).plus(0, 1, 0);
            new Fill(start, start.plus(Vec3i.fromDirection(Direction.SOUTH).mult(4)), "minecraft:stone_brick_stairs")
                    .dataValue("0") // face east
                    .runIn(process);
            start = start.plus(0, 2, 0);
            new Fill(start, start.plus(Vec3i.fromDirection(Direction.SOUTH).mult(4)), "minecraft:stone_brick_stairs")
                    .dataValue("4") // face east upside down
                    .runIn(process);
        }else{
            // create water line
            Vec3i start = position.plus(Vec3i.fromDirection(Direction.SOUTH).mult(2));
            Vec3i end = start.plus(Vec3i.fromDirection(Direction.EAST).mult(4));
            new Fill(start, end, "minecraft:water")
                    .runIn(process);
            // create stairs edges
            start = position.plus(0, 1, 0);
            new Fill(start, start.plus(Vec3i.fromDirection(Direction.EAST).mult(4)), "minecraft:stone_brick_stairs")
                    .dataValue("3") // face north
                    .runIn(process);
            start = start.plus(0, 2, 0);
            new Fill(start, start.plus(Vec3i.fromDirection(Direction.EAST).mult(4)), "minecraft:stone_brick_stairs")
                    .dataValue("7") // face north upside down
                    .runIn(process);
            start = position.plus(Vec3i.fromDirection(Direction.SOUTH).mult(4)).plus(0, 1, 0);
            new Fill(start, start.plus(Vec3i.fromDirection(Direction.EAST).mult(4)), "minecraft:stone_brick_stairs")
                    .dataValue("2") // face south
                    .runIn(process);
            start = start.plus(0, 2, 0);
            new Fill(start, start.plus(Vec3i.fromDirection(Direction.EAST).mult(4)), "minecraft:stone_brick_stairs")
                    .dataValue("6") // face south upside down
                    .runIn(process);
        }
    }
}
