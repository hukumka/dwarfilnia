package nktl.writer.blocks;

import nktl.generator.DwarfCube;
import nktl.math.geom.Direction;
import nktl.math.geom.Vec3i;
import nktl.server.MinecraftRMIProcess;
import nktl.server.commands.Command;
import nktl.server.commands.Fill;

import java.io.IOException;
import java.util.ArrayList;

public class Corridor implements DwarfBlock{
    private int data=0xf;

    public Corridor setData(int data){
        this.data = data;
        return this;
    }

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


        int[] bits = {
                DwarfCube.DIRECTION_EAST_BIT,
                DwarfCube.DIRECTION_SOUTH_BIT,
                DwarfCube.DIRECTION_WEST_BIT,
                DwarfCube.DIRECTION_NORTH_BIT
        };
        Vec3i center = position.plus(2, 2, 2);
        for(int i=0; i<4; ++i) {
            if(is(bits[i])){
                Fill f = new Fill(
                        position.plus(4, 1, 1),
                        position.plus(4, 3, 3),
                        "minecraft:air"
                );
                Fill fix_stair = null;
                Fill fix_stair2 = null;
                Fill fix_stair3 = null;
                Fill fix_stair4 = null;
                if(!is(bits[(i+3)%4])){ // test if not clockwise corridor
                    fix_stair = new Fill(
                            position.plus(4, 1, 0),
                            position.plus(4, 1, 0),
                            "minecraft:stone_brick_stairs"
                    ).dataValue(3);
                    fix_stair2 = new Fill(
                            position.plus(4, 3, 0),
                            position.plus(4, 3, 0),
                            "minecraft:stone_brick_stairs"
                    ).dataValue(7);
                }
                if(is(bits[(i+1)%4])){ // test if not clockwise corridor
                    fix_stair3 = new Fill(
                            position.plus(0, 1, 0),
                            position.plus(0, 1, 0),
                            "minecraft:stone_brick_stairs"
                    ).dataValue(1);
                    fix_stair4 = new Fill(
                            position.plus(0, 3, 0),
                            position.plus(0, 3, 0),
                            "minecraft:stone_brick_stairs"
                    ).dataValue(5);
                }
                for(int j=0; j<i; ++j){
                    f.rotate90(center.x, center.z);
                    if(fix_stair != null){
                        fix_stair.rotate90(center.x, center.z);
                        fix_stair2.rotate90(center.x, center.z);
                    }
                    if(fix_stair3 != null){
                        fix_stair3.rotate90(center.x, center.z);
                        fix_stair4.rotate90(center.x, center.z);
                    }
                }
                commands.add(f);
                if(fix_stair != null){
                    commands.add(fix_stair);
                    commands.add(fix_stair2);
                }
                if(fix_stair3 != null){
                    commands.add(fix_stair3);
                    commands.add(fix_stair4);
                }
            }
        }

        for(Fill c: commands){
            process.write(c.toCommandString());
        }
    }

    private boolean is(int bit){
        return (data & bit) > 0;
    }
}
