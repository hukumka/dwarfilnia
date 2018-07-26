package nktl.writer.blocks;

import nktl.dwarf.DwarfCube;
import nktl.dwarf.DwarfDirection;
import nktl.math.geom.Direction;
import nktl.math.geom.Vec3i;
import nktl.server.MinecraftRMIProcess;
import nktl.server.commands.Fill;

import java.io.IOException;
import java.util.ArrayList;

public class Collector implements DwarfBlock{
    boolean hasWater = false;
    int sewers = 0;

    void get_features(DwarfCube cube){
        hasWater = cube.features().containsKey(DwarfCube.Feature.WATER);
        if(cube.features().containsKey(DwarfCube.Feature.SEWER)){
            sewers = cube.features().get(DwarfCube.Feature.SEWER);
        }
    }

    @Override
    public void placeAt(MinecraftRMIProcess process, Vec3i position) throws IOException {
        ArrayList<Fill> commands = new ArrayList<>();
        if(hasWater) {
            commands.add(
                    new Fill(position, position.plus(4, 4, 3), "minecraft:water")
            );
            commands.add(
                    new Fill(position, position.plus(3, 4, 4), "minecraft:air")
            );
        }else{
            commands.add(
                    new Fill(position, position.plus(4, 4, 4), "minecraft:air")
            );
        }
        commands.add(
                new Fill(position.plus(0, 0, 0), position.plus(0, 4, 0), "minecraft:stone")
                        .dataValue(6)
        );
        commands.add(
                new Fill(position.plus(4, 0, 0), position.plus(4, 4, 0), "minecraft:stone")
                        .dataValue(6)
        );
        commands.add(
                new Fill(position.plus(0, 0, 4), position.plus(0, 4, 4), "minecraft:stone")
                        .dataValue(6)
        );
        commands.add(
                new Fill(position.plus(4, 0, 4), position.plus(4, 4, 4), "minecraft:stone")
                        .dataValue(6)
        );
        // create ladders
        commands.add(
                Builder.createStairs(position.plus(1, 0, 0), position.plus(3, 0, 0), Direction.NORTH, false)
        );
        commands.add(
                Builder.createStairs(position.plus(1, 4, 0), position.plus(3, 4, 0), Direction.NORTH, true)
        );
        commands.add(
                Builder.createStairs(position.plus(1, 0, 4), position.plus(3, 0, 4), Direction.SOUTH, false)
        );
        commands.add(
                Builder.createStairs(position.plus(1, 4, 4), position.plus(3, 4, 4), Direction.SOUTH, true)
        );
        commands.add(
                Builder.createStairs(position.plus(0, 0, 1), position.plus(0, 0, 3), Direction.WEST, false)
        );
        commands.add(
                Builder.createStairs(position.plus(0, 4, 1), position.plus(0, 4, 3), Direction.WEST, true)
        );
        commands.add(
                Builder.createStairs(position.plus(4, 0, 1), position.plus(4, 0, 3), Direction.EAST, false)
        );
        commands.add(
                Builder.createStairs(position.plus(4, 4, 1), position.plus(4, 4, 3), Direction.EAST, true)
        );

        if((sewers&DwarfDirection.BIT_POS_X)>0){
            commands.add(
                    new Fill(position.plus(4, 0, 2), position.plus(4, 4, 2), "minecraft:air")
            );
        }
        if((sewers&DwarfDirection.BIT_NEG_X)>0){
            commands.add(
                    new Fill(position.plus(0, 0, 2), position.plus(0, 4, 2), "minecraft:air")
            );
        }
        if((sewers&DwarfDirection.BIT_NEG_Y)>0){
            commands.add(
                    new Fill(position.plus(2, 0, 0), position.plus(2, 4, 0), "minecraft:air")
            );
        }
        if((sewers&DwarfDirection.BIT_POS_Y)>0){
            commands.add(
                    new Fill(position.plus(2, 0, 4), position.plus(2, 4, 4), "minecraft:air")
            );
        }

        // create sewers
        for(Fill c: commands){
            c.runIn(process);
        }
    }
}
