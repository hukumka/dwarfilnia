package nktl.generator;

import nktl.math.geom.Vec3i;

public class DwarfCube {
    int type = 0;
    Vec3i position = new Vec3i();

    DwarfCube(){}

    DwarfCube(Vec3i position){
        this.position.copy(position);
    }

    public int getType() {
        return type;
    }

    public Vec3i getPosition() {
        return position;
    }
}
