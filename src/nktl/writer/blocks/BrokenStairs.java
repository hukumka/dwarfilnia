package nktl.writer.blocks;

import nktl.dwarf.DwarfCube;
import nktl.dwarf.DwarfDirection;
import nktl.math.geom.Vec3i;
import nktl.server.MinecraftRMIProcess;
import nktl.server.commands.BlockData;
import nktl.server.commands.Fill;
import nktl.server.commands.states.Facing;
import nktl.server.commands.states.Half;
import nktl.server.commands.states.SlabType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class BrokenStairs extends Stairs{
    double changeChance = 0.4;
    double stairsChance = 1.0;
    double halfBlockChance = 1.0;

    boolean isUpper=false;
    boolean isLower=false;

    private Random random;

    public BrokenStairs(Random random) {
        this.random = random;
    }

    public BrokenStairs setUpper(boolean is){
        isUpper = is;
        return this;
    }
    public BrokenStairs setLower(boolean is){
        isLower = is;
        return this;
    }

    @Override
    public BrokenStairs get_features(DwarfCube cube){
        super.get_features(cube);
        int destruction = cube.features().get(DwarfCube.Feature.DESTRUCTION);
        setUpper((destruction&DwarfDirection.BIT_POS_Y)>0);
        setLower((destruction&DwarfDirection.BIT_NEG_Y)>0);
        return this;
    }

    @Override
    public void placeAt(MinecraftRMIProcess process, Vec3i pos) throws IOException{
        super.placeAt(process, pos);
        ArrayList<Fill> commands = new ArrayList<>();
        if(isUpper) {
            commands.addAll(addGarbage(pos, true));
        }
        if(isLower){
            commands.addAll(addGarbage(pos, false));
        }
        for(Fill f: commands){
            for(int i=0; i<direction.rotationCount(); ++i){
                f.rotate90Y(pos.x + 2, pos.z + 2);
            }
            f.runIn(process);
        }
    }

    private ArrayList<Fill> addGarbage(Vec3i pos, boolean isUpper){
        ArrayList<Fill> commands = new ArrayList<>();
        Vec3i center = pos.plus(2, 2, 2);
        // add base garbage
        for(int i=0; i<3; ++i){
            Vec3i from_offset = new Vec3i(i-2, i-1, -1);
            Vec3i to_offset = from_offset.plus(0, 0, 2);
            if(!isUpper){
                from_offset.multIn(-1);
                from_offset.x--;
                to_offset.multIn(-1);
                to_offset.x--;
            }
            commands.add(new Fill(center.plus(to_offset), center.plus(from_offset), new BlockData("minecraft:cobblestone")));
        }
        if(isUpper){
            commands.add(new Fill(center.plus(1, 2, 1), center.plus(-1, 2, -1), new BlockData("minecraft:cobblestone")));
        }else{
            commands.add(new Fill(center.plus(1, -2, 1), center.plus(-1, -2, -1), new BlockData("minecraft:cobblestone")));
        }
        // add random garbage
        for(int dx=-2; dx<0; ++dx){
            for(int dy=0; dy<3; ++dy){
                if(dx == -2 && dy == 1){
                    continue;
                }
                for(int dz=-1; dz<2; ++dz) {
                    Vec3i offset = new Vec3i(dx, dy, dz);
                    if(!isUpper){
                        offset.multIn(-1);
                        offset.x--;
                    }
                    if(Math.random() < changeChance) {
                        commands.add(addRandomGarbage(center.plus(offset)));
                    }
                }
            }
        }
        return commands;
    }

    private Fill addRandomGarbage(Vec3i pos){
        double total = stairsChance + halfBlockChance;
        double roll = Math.random();
        if(roll < stairsChance/total){
            // stairs
            return new Fill(pos, pos, randomStairs().addParam(randomHalf()).addParam(randomFacing()));
        }else{
            // half-block
            return new Fill(pos, pos, randomSlab().addParam(randomSlabType()));
        }
    }

    private static String[] possibleMaterial = {
            "cobblestone_",
            "stone_brick_",
            "stone_",
            "mossy_stone_brick_",
            "mossy_cobblestone_",
            "andesite_"
    };

    private BlockData randomStairs() {
        int ind = random.nextInt(possibleMaterial.length);
        return new BlockData("minecraft:" + possibleMaterial[ind]+"stairs");
    }

    private BlockData randomSlab() {
        int ind = random.nextInt(possibleMaterial.length);
        return new BlockData("minecraft:" + possibleMaterial[ind]+"slab");
    }

    private Half randomHalf(){
        return Half.values()[random.nextInt(Half.values().length)];
    }

    private SlabType randomSlabType(){
        return SlabType.values()[random.nextInt(SlabType.values().length)];
    }

    private Facing randomFacing() {
        return Facing.values()[random.nextInt(Facing.values().length)];
    }
}
