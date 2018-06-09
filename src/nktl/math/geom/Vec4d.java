package nktl.math.geom;

/**
 * Created by Zheka Grushevskiy, NAKATEEL, 07.02.2018.
 */
public class Vec4d {
    public double x, y, z, w;

    public Vec4d(){}

    public Vec4d(double x, double y, double z, double w){
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vec4d copy(){
        return new Vec4d(x, y, z, w);
    }

    public void copy(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public void unit(){
        double length = length();
        x /= length;
        y /= length;
        z /= length;
        w /= length;
    }

    public double length(){
        return Math.sqrt(x*x + y*y + z*z + w*w);
    }

    public void norm(){
        x /= w;
        y /= w;
        z /= w;
        w = 1.;
    }
}
