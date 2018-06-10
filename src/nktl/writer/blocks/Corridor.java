package nktl.writer.blocks;

import nktl.generator.DwarfCube;
import nktl.math.geom.Vec3i;
import nktl.server.MinecraftRMIProcess;
import nktl.server.commands.Command;
import nktl.server.commands.Fill;

import java.io.IOException;
import java.util.ArrayList;

public class Corridor implements DwarfBlock{
    private DwarfCube cube=null;

    @Override
    public void placeAt(MinecraftRMIProcess process, Vec3i position) throws IOException{
        ArrayList<Fill> commands = new ArrayList<>();

        // fill all with air
        commands.add(
                new Fill(position, position.plus(4, 4, 4), "minecraft:air")
        );
        // create floor
        commands.add(
                new Fill(position, position.plus(4, 0, 4), "minecraft:stonebrick")
        );
        // create ceiling
        commands.add(
                new Fill(position.plus(0, 4, 0), position.plus(4, 4, 4), "minecraft:stonebrick")
        );

        // create edge ladders
        commands.add(
                new Fill(position.plus(0, 1, 0), position.plus(3, 1, 0), "minecraft:stone_brick_stairs")
                        .dataValue(3)
        );
        commands.add(
                new Fill(position.plus(4, 1, 0), position.plus(4, 1, 3), "minecraft:stone_brick_stairs")
                        .dataValue(0)
        );
        commands.add(
                new Fill(position.plus(4, 1, 4), position.plus(1, 1, 4), "minecraft:stone_brick_stairs")
                        .dataValue(2)
        );
        commands.add(
                new Fill(position.plus(0, 1, 4), position.plus(0, 1, 1), "minecraft:stone_brick_stairs")
                        .dataValue(1)
        );
        // create edge ladders upside down
        commands.add(
                new Fill(position.plus(0, 3, 0), position.plus(3, 3, 0), "minecraft:stone_brick_stairs")
                        .dataValue(7)
        );
        commands.add(
                new Fill(position.plus(4, 3, 0), position.plus(4, 3, 3), "minecraft:stone_brick_stairs")
                        .dataValue(4)
        );
        commands.add(
                new Fill(position.plus(4, 3, 4), position.plus(1, 3, 4), "minecraft:stone_brick_stairs")
                        .dataValue(6)
        );
        commands.add(
                new Fill(position.plus(0, 3, 4), position.plus(0, 3, 1), "minecraft:stone_brick_stairs")
                        .dataValue(5)
        );



        for(Command c: commands){
            process.write(c.toCommandString());
        }
    }
}
