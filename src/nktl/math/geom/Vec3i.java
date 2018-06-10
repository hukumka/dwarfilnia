package nktl.math.geom;

import java.util.Objects;

public class Vec3i {
    public int x, y, z;

    public Vec3i(){}

    public Vec3i(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3i plus(int x, int y, int z){
        return new Vec3i(x + this.x, y + this.y, z + this.z);
    }

    public Vec3i plus(Vec3i other){
        return new Vec3i(x + other.x, y + other.y, z + other.z);
    }

    public Vec3i mult(int k){
        return new Vec3i(x*k, y*k, z*k);
    }

    public Vec3i copy(){
        return new Vec3i(x, y, z);
    }

    public void copy(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * create vector rotated around Z axis counterclockwise
     * @param ax: axis x coordinate
     * @param az: axis y coordinate
     * @return rotated vector
     */
    public Vec3i rotateAroundY90(int ax, int az){
        return new Vec3i(ax + (z - az), y, az + (ax - x));
    }

    public Vec3d unit(){
        double length = length();
        return new Vec3d(x/length, y/length, z/length);
    }

    public static Vec3i fromDirection(Direction d){
        switch (d){
            case NORTH: return new Vec3i(0, 0, -1);
            case SOUTH: return new Vec3i(0, 0, 1);
            case WEST: return new Vec3i(-1, 0, 0);
            case EAST: return new Vec3i(1, 0, 0);
        }
        return new Vec3i(0, 0, 0);
    }



    @Override
    public boolean equals(Object other){
        if(other == this){
            return true;
        }else if(other instanceof Vec3i) {
            Vec3i o = (Vec3i) other;
            return x == o.x && y == o.y && z == o.z;
        }else{
            return false;
        }
    }

    @Override
    public int hashCode(){
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString(){
        return String.format("Vec3i{%d, %d, %d}", x, y, z);
    }

    public double length(){
        return Math.sqrt(x*x + y*y + z*z);
    }


}
