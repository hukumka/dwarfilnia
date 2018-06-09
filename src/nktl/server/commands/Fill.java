package nktl.server.commands;

import nktl.math.geom.Vec3i;

public class Fill extends Command{
    private Vec3i from;
    private Vec3i to;

    private String block;
    private String additional_data = "0";
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

    public Fill dataValue(String data){
        this.additional_data = data;
        return this;
    }

    public Fill outline(){
        this.modifier = "outline";
        return this;
    }

    @Override
    public String toCommandString() {
        return String.format("fill %d %d %d %d %d %d %s %s %s", from.x, from.y, from.z, to.x, to.y, to.z, block, additional_data, modifier);
    }
}
