package nktl.writer.blocks;

import nktl.math.geom.Vec3i;
import nktl.server.MinecraftRMIProcess;
import nktl.server.commands.Fill;

import java.io.IOException;
import java.util.ArrayList;

public class SewerTurningCorridor implements DwarfBlock{
    private int rotationCount;

    public SewerTurningCorridor(int rotationCount){
        this.rotationCount = rotationCount;
    }

    @Override
    public void placeAt(MinecraftRMIProcess process, Vec3i pos) throws IOException {
        Vec3i center = pos.plus(2, 2, 2);
        ArrayList<Fill> commands = new ArrayList<>();

        // create air space
        commands.add(
                new Fill(pos, pos.plus(4, 4, 4), "minecraft:air")
        );
        // add floor
        commands.add(
                new Fill(pos, pos.plus(4, 0, 4), "minecraft:stonebrick")
        );
        // add ceiling
        commands.add(
                new Fill(pos.plus(0, 4, 0), pos.plus(4, 4, 4), "minecraft:stonebrick")
        );
        // add water line
        commands.add(
                new Fill(
                        pos.plus(2, 0, 0),
                        pos.plus(2, 0, 2),
                        "minecraft:water"
                )
        );
        commands.add(
                new Fill(
                        pos.plus(2, 0, 2),
                        pos.plus(4, 0, 2),
                        "minecraft:water"
                )
        );
        // add stairs edges
        commands.add(
                new Fill(
                        pos.plus(0, 1, 0),
                        pos.plus(0, 1, 4),
                        "minecraft:stone_brick_stairs"
                ).dataValue(1)
        );
        commands.add(
                new Fill(
                        pos.plus(1, 1, 4),
                        pos.plus(4, 1, 4),
                        "minecraft:stone_brick_stairs"
                ).dataValue(2)
        );
        commands.add(
                new Fill(
                        pos.plus(4, 1, 0),
                        pos.plus(4, 1, 0),
                        "minecraft:stone_brick_stairs"
                ).dataValue(3)
        );
        // add ceiling stairs edges
        commands.add(
                new Fill(
                        pos.plus(0, 3, 0),
                        pos.plus(0, 3, 4),
                        "minecraft:stone_brick_stairs"
                ).dataValue(5)
        );
        commands.add(
                new Fill(
                        pos.plus(1, 3, 4),
                        pos.plus(4, 3, 4),
                        "minecraft:stone_brick_stairs"
                ).dataValue(6)
        );
        commands.add(
                new Fill(
                        pos.plus(4, 3, 0),
                        pos.plus(4, 3, 0),
                        "minecraft:stone_brick_stairs"
                ).dataValue(7)
        );

        for(Fill c: commands){
            for(int i=0; i<rotationCount; ++i){
                c = c.rotate90(center.x, center.z);
            }
            c.runIn(process);
        }
    }
}
