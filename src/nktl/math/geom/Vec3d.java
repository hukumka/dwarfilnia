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

    public Vec3d(Vec3d src){
        this.x = src.x;
        this.y = src.y;
        this.z = src.z;
    }

    public Vec3d copy(){
        return new Vec3d(x, y, z);
    }

    public void copy(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void copy(Vec3d src) {
        this.x = src.x;
        this.y = src.y;
        this.z = src.z;
    }

    public Vec3d unit(){
        double length = length();
        return new Vec3d(x/length, y/length, z/length);
    }

    public void unitIn(){
        double length = length();
        x /= length;
        y /= length;
        z /= length;
    }

    public Vec3d multIn(double value){
        this.x *= value;
        this.y *= value;
        this.z *= value;
        return this;
    }

    public Vec3d mult(double value){
        return new Vec3d(x * value, y * value, z * value);
    }

    public Vec3d plus(Vec3d src){
        return new Vec3d(this.x + src.x, this.y + src.y, this.z + src.z);
    }

    public Vec3d plusIn(Vec3d src){
        this.x += src.x;
        this.y += src.y;
        this.z += src.z;
        return this;
    }

    public Vec3d minus(Vec3d src) {
        return new Vec3d(this.x - src.x, this.y - src.y, this.z - src.z);
    }

    public Vec3d minusIn(Vec3d src){
        this.x -= src.x;
        this.y -= src.y;
        this.z -= src.z;
        return this;
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
