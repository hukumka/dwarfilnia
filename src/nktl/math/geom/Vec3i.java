package nktl.math.geom;

public class Vec3i {
    public int x, y, z;

    public Vec3i(){}

    public Vec3i(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
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

    public double length(){
        return Math.sqrt(x*x + y*y + z*z);
    }


}
