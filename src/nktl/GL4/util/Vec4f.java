package nktl.GL4.util;

import java.util.Objects;

/**
 * Методы:
 * Копирование,
 * Перемножение,
 * Сложение,
 * Вычитание,
 *
 *
 * Created by Zheka Grushevskiy, NAKATEEL, 26.08.2016.
 */
public class Vec4f {
    public float x, y, z, w;

    private static final Vec4f temp1 = new Vec4f();

    public Vec4f(){}

    public Vec4f(float xyz, float w){
        this.x = this.y = this.z = xyz;
        this.w = w;
    }

    public Vec4f(float xyzw) {
        this.x = this.y = this.z = this.w = xyzw;
    }

    public Vec4f(float x, float y, float z, float w){
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vec4f(Vec4f src){
        this.x = src.x;
        this.y = src.y;
        this.z = src.z;
        this.w = src.w;
    }

    // Умножение матрицы на вектор
    private static void multiMV(Vec4f res, float[] m, Vec4f v) {
        res.x = m[0]*v.x + m[4]*v.y + m[ 8]*v.z + m[12]*v.w;
        res.y = m[1]*v.x + m[5]*v.y + m[ 9]*v.z + m[13]*v.w;
        res.z = m[2]*v.x + m[6]*v.y + m[10]*v.z + m[14]*v.w;
        res.w = m[3]*v.x + m[7]*v.y + m[11]*v.z + m[15]*v.w;
    }

    // Умножает матрицу на вектор src и сохраняет полученный вестор в res
    public Vec4f multiMVTo(Vec4f res, Mat4f mat){
        multiMV(res, mat.arr, this);
        return res;
    }

    public Vec4f multiMVIn(Mat4f mat, Vec4f src){
        multiMV(this, mat.arr, src);
        return this;
    }

    public Vec4f multiMVIn(Mat4f mat){
        synchronized (temp1){
            temp1.multiMVIn(mat, this);
            this.copy(temp1);
        }
        return this;
    }

    public Vec4f multiMVNew(Mat4f mat){
        return new Vec4f().multiMVIn(mat, this);
    }

    // Копирование
    public Vec4f copy(Vec4f src){
        this.x = src.x;
        this.y = src.y;
        this.z = src.z;
        this.w = src.w;
        return this;
    }

    public Vec4f copy(Vec3f vec3f, float w){
        this.w = w;
        return copy(vec3f);
    }

    public Vec4f copy(Vec3f src){
        this.x = src.x;
        this.y = src.y;
        this.z = src.z;
        return this;
    }

    public Vec4f copy(float x, float y, float z, float w){
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        return this;
    }

    public double length4d(){
        return Math.sqrt(x*x + y*y + z*z + w*w);
    }

    public double length3d(){
        return Math.sqrt(x*x + y*y + z*z);
    }

    public Vec4f unit3d() {
        double length = length3d();
        x/=length;
        y/=length;
        z/=length;
        return this;
    }

    public Vec4f unit4d() {
        double length = length4d();
        x/=length;
        y/=length;
        z/=length;
        w/=length;
        return this;
    }

    @Override
    public String toString() {
        return x + "; " + y + "; " + z + "; " + w;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vec4f vec4f = (Vec4f) o;
        return Float.compare(vec4f.x, x) == 0 &&
                Float.compare(vec4f.y, y) == 0 &&
                Float.compare(vec4f.z, z) == 0 &&
                Float.compare(vec4f.w, w) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, w);
    }
}
