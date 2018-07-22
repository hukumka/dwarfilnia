package nktl.dwarf;

import nktl.math.geom.Vec3i;

public enum DwarfDirection {

    POS_X(new Vec3i(1, 0, 0), true, 0b1),
    POS_Z(new Vec3i(0, 0, 1), true, 0b10),
    NEG_X(new Vec3i(-1, 0, 0), true, 0b100),
    NEG_Z(new Vec3i(0, 0, -1), true, 0b1000),
    POS_Y(new Vec3i(0, 1, 0), false, 0b10000),
    NEG_Y(new Vec3i(0, -1, 0), false, 0b100000);

    final Vec3i increment, decrement;
    public final boolean isHorizontal;
    public final int bit;

    DwarfDirection(Vec3i inc, boolean isHorizontal, int bit){
        this.increment = inc;
        this.decrement = new Vec3i(
                -inc.x, -inc.y, -inc.z
        );
        this.isHorizontal = isHorizontal;
        this.bit = bit;
    }

    DwarfDirection getBack(){
        switch (this) {
            case POS_X: return NEG_X;
            case POS_Y: return NEG_Y;
            case POS_Z: return NEG_Z;
            case NEG_X: return POS_X;
            case NEG_Y: return POS_Y;
            default: return POS_Z;
        }
    }

    DwarfDirection getLeft(){
        switch (this) {
            case POS_X: return NEG_Z;
            case POS_Z: return POS_X;
            case NEG_X: return POS_Z;
            default: return NEG_X;
        }
    }

    DwarfDirection getFront(){
        return this;
    }

    DwarfDirection getRight(){
        switch (this) {
            case POS_X: return POS_Z;
            case POS_Z: return NEG_X;
            case NEG_X: return NEG_Z;
            default: return POS_X;
        }
    }

    /*
        STATIC
     */

    public static final int
            BIT_POS_X = 0b1,
            BIT_POS_Z = 0b10,
            BIT_NEG_X = 0b100,
            BIT_NEG_Z = 0b1000,
            BIT_POS_Y = 0b10000,
            BIT_NEG_Y = 0b100000;

    public static int rotateDir180(int dir){
        return (dir & 0b110000) + (((((dir & 0b1111) * 0b10001) << 2) & 0b11110000) >> 4);
    }

    public static DwarfDirection[] getHorizontal(){
        return new DwarfDirection[]{
                POS_X, POS_Z, NEG_X, NEG_Z
        };
    }


}
