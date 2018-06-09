package nktl.math.geom;

/**
 * Created by Zheka Grushevskiy, NAKATEEL, 07.02.2018.
 */
public class Vec3d {
    public double x, y, z;

    public Vec3d(){}

    public Vec3d(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3d copy(){
        return new Vec3d(x, y, z);
    }

    public void copy(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void unit(){
        double length = length();
        x /= length;
        y /= length;
        z /= length;
    }

    public double length(){
        return Math.sqrt(x*x + y*y + z*z);
    }

    public double dot(Vec3d vec){
        return this.x*vec.x + this.y*vec.y + this.z*vec.z;
    }

    public void cross(Vec3d vec){
        double tx = this.y*vec.z - this.z*vec.y;
        double ty = this.z*vec.x - this.x*vec.z;
        this.z = this.x*vec.y - this.y*vec.x;
        this.x = tx;
        this.y = ty;
    }

    public void cross(Vec3d v1, Vec3d v2){
        this.z = v1.y*v2.z - v1.z*v2.y;
        this.x = v1.z*v2.x - v1.x*v2.z;
        this.y = v1.x*v2.y - v1.y*v2.x;
    }
}
