package nktl.math.geom;

/**
 * Created by Zheka Grushevskiy, NAKATEEL, 07.02.2018.
 */
public class Vec2d {
    public double x, y;

    public Vec2d(){}

    public Vec2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vec2d copy(){
        return new Vec2d(x, y);
    }

    public void copy(Vec2d src) {
        this.x = src.x;
        this.y = src.y;
    }

    public void copy(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void unit(){
        double len = length();
        x /= len;
        y /= len;
    }

    public double length(){
        return Math.sqrt(x*x + y*y);
    }

    public static double cross(Vec2d v1, Vec2d v2){
        return v1.x*v2.y - v2.x*v1.y;
    }

    public static double cross(double x1, double y1, double x2, double y2){
        return x1*y2 - y1*x2;
    }

    public static double dot(Vec2d v1, Vec2d v2){
        return v1.x*v2.x + v1.y*v2.y;
    }

    public static double dot(double x1, double y1, double x2, double y2){
        return x1*x2 + y1*y2;
    }

    @Override
    public String toString() {
        return "Vec2d{" + x + "; " + y + '}';
    }
}
