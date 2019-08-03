package nktl.server.commands;

import nktl.math.geom.Vec3i;

public class Fill extends Command{
    private Vec3i from;
    private Vec3i to;

    private BlockData block;
    private String modifier = "";

    public Fill(Vec3i from, Vec3i to, BlockData block){
        this.from = from;
        this.to = to;
        this.block = block;
    }

    public Fill replace(String replace){
        this.modifier = "replace " + replace;
        return this;
    }

    public Fill outline(){
        this.modifier = "outline";
        return this;
    }

    public Fill rotate90Y(int x, int z){
        from = from.rotateAroundY90(x, z);
        to = to.rotateAroundY90(x, z);
        block = block.rotate90Y();
        return this;
    }


    @Override
    public String toCommandString() {
        return String.format("fill %d %d %d %d %d %d %s %d %s", from.x, from.y, from.z, to.x, to.y, to.z, block.toString(), modifier);
    }
}
