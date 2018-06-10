package nktl.server.commands;

import nktl.math.geom.Vec3i;

public class CloneBlock extends Command {
    private Vec3i from;
    private Vec3i to;
    public CloneBlock(Vec3i from, Vec3i to){
        this.from = from;
        this.to = to;
    }

    @Override
    public String toCommandString(){
        return String.format(
                "clone %d %d %d %d %d %d %d %d %d",
                from.x, from.y, from.z,
                from.x+4, from.y+4, from.z+4,
                to.x, to.y, to.z
        );
    }
}
