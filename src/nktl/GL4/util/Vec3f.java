package nktl.GL4.util;

import nktl.math.geom.Vec3i;

import java.util.Objects;

/**
 * Created by Zheka Grushevskiy, NAKATEEL, 26.08.2016.
 */
public class Vec3f {

    public float x, y, z;

    public Vec3f(){}
    public Vec3f(float value){
        this.x = this.y = this.z = value;
    }

    public Vec3f(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3f(Vec3f vec3f){
        this.x = vec3f.x;
        this.y = vec3f.y;
        this.z = vec3f.z;
    }

    public Vec3f vecProdIn(Vec3f src1, Vec3f src2){
        this.x = src1.y * src2.z - src1.z * src2.y;
        this.y = src1.z * src2.x - src1.x * src2.z;
        this.z = src1.x * src2.y - src1.y * src2.x;
        return this;
    }

    public Vec3f vecProdTo(Vec3f res, Vec3f src){
        res.vecProdIn(this, src);
        return res;
    }

    public void multIn(double value) {
        this.x *= value;
        this.y *= value;
        this.z *= value;
    }

    public Vec3f plus(Vec3f src) {
        return new Vec3f(this.x + src.x, this.y + src.y, this.z + src.z);
    }

    public Vec3f plus(Vec3i src) {
        return new Vec3f(this.x + src.x, this.y + src.y, this.z + src.z);
    }

    public void plusIn(Vec3f src) {
        this.x += src.x;
        this.y += src.y;
        this.z += src.z;
    }

    public void plusIn(Vec3i src) {
        this.x += src.x;
        this.y += src.y;
        this.z += src.z;
    }

    public Vec3f minus(Vec3f src) {
        return new Vec3f(
                this.x - src.x,
                this.y - src.y,
                this.z - src.z);
    }

    // Копирование
    public void copy(Vec3f src){
        this.x = src.x;
        this.y = src.y;
        this.z = src.z;
    }

    public void copy(Vec4f src){
        this.x = src.x;
        this.y = src.y;
        this.z = src.z;
    }

    public Vec3f copy(){
        return new Vec3f(x, y, z);
    }

    public Vec3f unit(){
        normalize();
        return this;
    }

    public double length(){
        return Math.sqrt(x*x + y*y + z*z);
    }

    public static void multiMV(Vec3f res, Mat4f m, Vec3f v) {
        res.x = m.arr[0]*v.x + m.arr[4]*v.y + m.arr[ 8]*v.z;
        res.y = m.arr[1]*v.x + m.arr[5]*v.y + m.arr[ 9]*v.z;
        res.z = m.arr[2]*v.x + m.arr[6]*v.y + m.arr[10]*v.z;
    }

    public void normalize(){
        double amp = Math.sqrt(x*x + y*y + z*z);
        x /= amp;
        y /= amp;
        z /= amp;
    }

    @Override
    public String toString() {
        return x + "; " + y + "; " + z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vec3f vec3f = (Vec3f) o;
        return Float.compare(vec3f.x, x) == 0 &&
                Float.compare(vec3f.y, y) == 0 &&
                Float.compare(vec3f.z, z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}
