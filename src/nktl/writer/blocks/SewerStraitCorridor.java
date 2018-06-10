package nktl.writer.blocks;

import nktl.math.geom.Direction;
import nktl.math.geom.Vec3i;
import nktl.server.MinecraftRMIProcess;
import nktl.server.commands.Command;
import nktl.server.commands.Fill;

import java.io.IOException;
import java.util.ArrayList;

public class SewerStraitCorridor implements DwarfBlock {
    boolean isNorthSouth;

    public SewerStraitCorridor(boolean isNorthSouth){
        this.isNorthSouth = isNorthSouth;
    }

    @Override
    public void placeAt(MinecraftRMIProcess process, Vec3i position) throws IOException {
        ArrayList<Fill> commands = new ArrayList<>();
        Vec3i center = position.plus(2, 2, 2);

        // create space
        commands.add(new Fill(position, position.plus(4, 4, 4), "minecraft:air"));
        // create floor
        commands.add(new Fill(position, position.plus(4, 0, 4), "minecraft:stonebrick"));
        // create ceiling
        commands.add(new Fill(position.plus(0, 4, 0), position.plus(4, 4, 4), "minecraft:stonebrick"));

        // create water line
        Vec3i start = position.plus(Vec3i.fromDirection(Direction.EAST).mult(2));
        Vec3i end = start.plus(Vec3i.fromDirection(Direction.SOUTH).mult(4));
        commands.add(new Fill(start, end, "minecraft:water"));

        // create stairs edges
        start = position.plus(0, 1, 0);
        commands.add(
                new Fill(start, start.plus(Vec3i.fromDirection(Direction.SOUTH).mult(4)), "minecraft:stone_brick_stairs")
                        .dataValue(1) // face west
        );
        start = start.plus(0, 2, 0);
        commands.add(
                new Fill(start, start.plus(Vec3i.fromDirection(Direction.SOUTH).mult(4)), "minecraft:stone_brick_stairs")
                        .dataValue(5) // face west upside down
        );
        start = position.plus(Vec3i.fromDirection(Direction.EAST).mult(4)).plus(0, 1, 0);
        commands.add(
                new Fill(start, start.plus(Vec3i.fromDirection(Direction.SOUTH).mult(4)), "minecraft:stone_brick_stairs")
                        .dataValue(0) // face east
        );
        start = start.plus(0, 2, 0);
        commands.add(
                new Fill(start, start.plus(Vec3i.fromDirection(Direction.SOUTH).mult(4)), "minecraft:stone_brick_stairs")
                        .dataValue(4) // face east upside down
        );

        for(Fill c: commands){
            if(isNorthSouth)
                c = c.rotate90(center.x, center.z);

            c.runIn(process);
        }
    }
}
