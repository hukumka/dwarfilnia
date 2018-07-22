package nktl.writer.blocks;

import nktl.dwarf.DwarfCube;
import nktl.dwarf.DwarfDirection;
import nktl.math.geom.Direction;
import nktl.math.geom.Vec3i;
import nktl.server.MinecraftRMIProcess;
import nktl.server.commands.Fill;

import java.io.IOException;
import java.util.ArrayList;

public class Corridor implements DwarfBlock{
    public static Corridor from_dwarf_cube(DwarfCube cube) {
        Corridor block = new Corridor();
        if (cube.features().containsKey(DwarfCube.Feature.WAY)) {
            int ways = cube.features().get(DwarfCube.Feature.WAY);
            boolean[] is_open = {
                    (ways & DwarfDirection.BIT_POS_X) > 0, // EAST
                    (ways & DwarfDirection.BIT_NEG_Z) > 0, // NORTH
                    (ways & DwarfDirection.BIT_NEG_X) > 0, // WEST
                    (ways & DwarfDirection.BIT_POS_Z) > 0, // SOUTH
            };
            block.set_ways(is_open);
        }
        return block;
    }


    // E N W S
    boolean[] is_open = {false, false, false, false};

    public Corridor set_ways(boolean[] ways){
        is_open = ways;
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

        Vec3i center = position.plus(2, 2, 2);
        // create stairs
        commands.addAll(generateStairs(center, is_open));

        for(Fill f: commands){
            f.runIn(process);
        }
    }

    private ArrayList<Fill> generateStairs(Vec3i center, boolean[] is_open){
        ArrayList<Fill> commands = new ArrayList<>();
        for(int i=0; i<4; ++i){
            boolean backOpen = is_open[(i+2)%4];
            boolean leftOpen = is_open[(i+5)%4];
            for(Fill f: buildCorner(center, backOpen, leftOpen)){
                for(int j=0; j<i; ++j){
                    f.rotate90Y(center.x, center.z);
                }
                commands.add(f);
            }
        }
        return commands;
    }

    private ArrayList<Fill> buildCorner(Vec3i center, boolean backOpen, boolean leftOpen){
        ArrayList<Fill> commands = new ArrayList<>();
        // first iteration is floor
        // second is ceiling
        //
        for(int i=0; i<2; ++i) {
            int innerHeight = (i==0)? -1: 1;
            int outerHeight = (i==0)? -2: 2;
            boolean innerUpsideDown = i != 0;
            boolean outerUpsideDown = i != 1;
            if (backOpen && leftOpen) {
                commands.add(Builder.createStairsSingle(center.plus(-2, innerHeight, -2), Direction.WEST, innerUpsideDown));
                commands.add(Builder.createStairsSingle(center.plus(-2, outerHeight, -2), Direction.EAST, outerUpsideDown));
            }
            if(!leftOpen) {
                commands.add(Builder.createStairs(center.plus(-2, innerHeight, -2), center.plus(0, innerHeight, -2), Direction.NORTH, innerUpsideDown));
                commands.add(Builder.createStairs(center.plus(-2, outerHeight, -2), center.plus(0, outerHeight, -2), Direction.SOUTH, outerUpsideDown));
            }
            if(!backOpen){
                commands.add(Builder.createStairs(center.plus(-2, innerHeight, -2), center.plus(-2, innerHeight, -1), Direction.WEST, innerUpsideDown));
                commands.add(Builder.createStairs(center.plus(-2, outerHeight, -2), center.plus(-2, outerHeight, -1), Direction.EAST, outerUpsideDown));
            }
        }
        return commands;
    }
}
