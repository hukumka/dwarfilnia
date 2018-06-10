package nktl.writer.blocks;

import nktl.math.geom.Vec3i;
import nktl.server.MinecraftRMIProcess;
import nktl.server.commands.Fill;

import java.io.IOException;
import java.util.ArrayList;

public class SewerCrossingCorridor implements DwarfBlock {
    @Override
    public void placeAt(MinecraftRMIProcess process, Vec3i pos) throws IOException {
        ArrayList<Fill> commands = new ArrayList<>();

        // create space
        commands.add(new Fill(pos, pos.plus(4, 4, 4), "minecraft:air"));
        // create floor
        commands.add(new Fill(pos, pos.plus(4, 0, 4), "minecraft:stonebrick"));
        // create ceiling
        commands.add(new Fill(pos.plus(0, 4, 0), pos.plus(4, 4, 4), "minecraft:stonebrick"));
        // add water line
        commands.add(
                new Fill(
                        pos.plus(2, 0, 0),
                        pos.plus(2, 0, 4),
                        "minecraft:water"
                )
        );
        commands.add(
                new Fill(
                        pos.plus(0, 0, 2),
                        pos.plus(4, 0, 2),
                        "minecraft:water"
                )
        );
        // add stairs
        commands.add(
                new Fill(
                        pos.plus(4, 1, 0),
                        pos.plus(4, 1, 0),
                        "minecraft:stone_brick_stairs"
                ).dataValue(3)
        );
        commands.add(
                new Fill(
                        pos.plus(0, 1, 4),
                        pos.plus(0, 1, 4),
                        "minecraft:stone_brick_stairs"
                ).dataValue(2)
        );
        commands.add(
                new Fill(
                        pos.plus(4, 1, 4),
                        pos.plus(4, 1, 4),
                        "minecraft:stone_brick_stairs"
                ).dataValue(0)
        );
        commands.add(
                new Fill(
                        pos.plus(0, 1, 0),
                        pos.plus(0, 1, 0),
                        "minecraft:stone_brick_stairs"
                ).dataValue(1)
        );

        // add stairs
        commands.add(
                new Fill(
                        pos.plus(4, 3, 0),
                        pos.plus(4, 3, 0),
                        "minecraft:stone_brick_stairs"
                ).dataValue(7)
        );
        commands.add(
                new Fill(
                        pos.plus(0, 3, 4),
                        pos.plus(0, 3, 4),
                        "minecraft:stone_brick_stairs"
                ).dataValue(6)
        );
        commands.add(
                new Fill(
                        pos.plus(4, 3, 4),
                        pos.plus(4, 3, 4),
                        "minecraft:stone_brick_stairs"
                ).dataValue(4)
        );
        commands.add(
                new Fill(
                        pos.plus(0, 3, 0),
                        pos.plus(0, 3, 0),
                        "minecraft:stone_brick_stairs"
                ).dataValue(5)
        );

        for(Fill c: commands){
            c.runIn(process);
        }
    }
}
