package nktl.math.geom;

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

    public double length(){
        return Math.sqrt(x*x + y*y + z*z);
    }


}
