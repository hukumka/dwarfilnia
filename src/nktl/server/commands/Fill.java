package nktl.server.commands;

import nktl.math.geom.Vec3i;

public class Fill extends Command{
    private Vec3i from;
    private Vec3i to;

    private String block;
    private int additional_data = 0;
    private String modifier = "";

    public Fill(Vec3i from, Vec3i to, String block){
        this.from = from;
        this.to = to;
        this.block = block;
    }

    public Fill replace(String replace){
        this.modifier = "replace " + replace;
        return this;
    }

    public Fill dataValue(int data){
        this.additional_data = data;
        return this;
    }

    public Fill outline(){
        this.modifier = "outline";
        return this;
    }

    public Fill rotate90(int x, int z){
        from = from.rotateAroundY90(x, z);
        to = to.rotateAroundY90(x, z);
        if(block.equals("minecraft:stone_brick_stairs")){
            int direction = additional_data & 3;
            int new_direction = 0;
            switch (direction){
                case 0:
                    new_direction = 3;
                    break;
                case 1:
                    new_direction = 2;
                    break;
                case 2:
                    new_direction = 0;
                    break;
                case 3:
                    new_direction = 1;
                    break;
            }
            // replace direction with new direction
            additional_data = (additional_data ^ direction) | new_direction;
        }
        return this;
    }

    public Fill rotate180(int x, int z){
        return rotate90(x, z).rotate90(x, z);
    }
    public Fill rotate270(int x, int z){
        return rotate90(x, z).rotate90(x, z).rotate270(x, z);
    }

    @Override
    public String toCommandString() {
        return String.format("fill %d %d %d %d %d %d %s %d %s", from.x, from.y, from.z, to.x, to.y, to.z, block, additional_data, modifier);
    }
}
