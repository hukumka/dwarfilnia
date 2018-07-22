package nktl.writer.blocks;


import nktl.dwarf.DwarfCube;
import nktl.dwarf.DwarfDirection;
import nktl.math.geom.Direction;
import nktl.math.geom.Vec3i;
import nktl.server.MinecraftRMIProcess;
import nktl.server.commands.Fill;

import java.io.IOException;
import java.util.ArrayList;

public class Stairs implements DwarfBlock{
    Direction direction=Direction.EAST;

    public Stairs get_features(DwarfCube cube){
        if(cube.features().containsKey(DwarfCube.Feature.WAY)){
            int way = cube.features().get(DwarfCube.Feature.WAY);
            System.out.println(way);
            Direction dir = null;
            if((way&DwarfDirection.BIT_POS_X)>0){
                dir = Direction.EAST;
            }else if((way&DwarfDirection.BIT_NEG_X) > 0) {
                dir = Direction.WEST;
            }else if((way&DwarfDirection.BIT_POS_Z) > 0) {
                dir = Direction.SOUTH;
            }else if((way&DwarfDirection.BIT_NEG_Z) > 0) {
                dir = Direction.NORTH;
            }
            if(dir != null){
                this.setDirection(dir);
            }
        }
        return this;
    }

    public Stairs setDirection(Direction direction){
        this.direction = direction;
        return this;
    }

    @Override
    public void placeAt(MinecraftRMIProcess process, Vec3i pos) throws IOException {
        ArrayList<Fill> commands = new ArrayList<>();
        // create air
        commands.add(
                new Fill(pos.plus(0, 0, 1), pos.plus(4, 4, 3), "minecraft:air")
        );
        // create stairs
        for(int i=0; i<4; ++i){
            commands.add(
                    new Fill(pos.plus(i, i+1, 1), pos.plus(i, i+1, 3), "minecraft:stone_brick_stairs")
                            .dataValue(0)
            );
            commands.add(
                    new Fill(pos.plus(i, i, 1), pos.plus(i, i, 3), "minecraft:stone_brick_stairs")
                            .dataValue(5)
            );
        }
        commands.add(
                new Fill(pos.plus(4, 4, 1), pos.plus(4, 4, 3), "minecraft:stone_brick_stairs")
                        .dataValue(5)
        );
        commands.add(
                new Fill(pos.plus(4, 0, 1), pos.plus(4, 0, 3), "minecraft:stone_brick_stairs")
                        .dataValue(0)
        );
        commands.add(
                new Fill(pos.plus(-1, 4, 1), pos.plus(-1, 4, 3), "minecraft:stone_brick_stairs")
                        .dataValue(5)
        );
        // create walls
        commands.add(
                new Fill(pos, pos.plus(4, 4, 0), "minecraft:stonebrick")
                    .replace("minecraft:air")
        );
        commands.add(
                new Fill(pos.plus(0, 0, 4), pos.plus(4, 4, 4), "minecraft:stonebrick")
                        .replace("minecraft:air")
        );
        // create pillars
        commands.add(
                new Fill(pos.plus(-1, 0, 0), pos.plus(-1, 4, 0), "minecraft:stone")
                    .dataValue(6)
        );
        commands.add(
                new Fill(pos.plus(-1, 0, 4), pos.plus(-1, 4, 4), "minecraft:stone")
                        .dataValue(6)
        );
        commands.add(
                new Fill(pos.plus(5, 0, 0), pos.plus(5, 4, 0), "minecraft:stone")
                        .dataValue(6)
        );
        commands.add(
                new Fill(pos.plus(5, 0, 4), pos.plus(5, 4, 4), "minecraft:stone")
                        .dataValue(6)
        );

        Vec3i center = pos.plus(2, 2, 2);
        for(Fill c: commands){
            for(int i=0; i<direction.rotationCount(); ++i)
                c.rotate90Y(center.x, center.z);
            c.runIn(process);
        }

    }
}
