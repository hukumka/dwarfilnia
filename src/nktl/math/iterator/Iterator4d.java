package nktl.math.iterator;

import nktl.math.geom.Vec4d;

/**
 * Created by Zheka Grushevskiy, NAKATEEL, 04.09.2017.
 */
public interface Iterator4d extends Iterator3d {
    double w();

    /*
        Генераторы:
     */
    @Deprecated static Iterator4dV getIterator(Vec4d... data){
        return over(data);
    }
    @Deprecated static Iterator4dA getIterator(double[] x, double[] y, double[] z, double[] w){
        return over(x, y, z, w);
    }
    @Deprecated static Iterator4dR getIterator(double x_start, double x_stop, double[] y, double[] z, double[] w){
        return over(x_start, x_stop, y, z, w);
    }


    static Iterator4dV over(Vec4d... data){
        if (data == null || data.length == 0)
            return null;
        return new Iterator4dV(data);
    }

    static Iterator4dA over(double[] x, double[] y, double[] z, double[] w){
        if (x == null || y == null || z == null || w == null ||
            x.length != y.length || x.length != z.length || x.length != w.length ||
                x.length == 0)
            return null;
        return new Iterator4dA(x, y, z, w);
    }

    static Iterator4dR over(double x_start, double x_stop, double[] y, double[] z, double[] w){
        if (x_start == x_stop ||
            y == null || y.length == 0 ||
            z == null || z.length == 0 ||
            w == null || w.length == 0)
            return null;
        return new Iterator4dR(x_start, x_stop, y, z, w);
    }

    /*
        РЕЛАЗИАЦИИ ИТЕРАТОРОВ
     */

    /**
     *  Итератор по массиву векторов.
     */
    class Iterator4dV extends AbsIterator implements Iterator4d {
        private Vec4d[] data;

        Iterator4dV(Vec4d[] data){
            this.data = data;
        }

        @Override
        public void last() {
            position = data.length-1;
        }

        @Override
        public double x(){
            return this.data[position].x;
        }
        @Override
        public double y(){
            return this.data[position].y;
        }
        @Override
        public double z(){
            return this.data[position].z;
        }
        @Override
        public double w(){
            return this.data[position].w;
        }
        @Override
        public int length() {
            return data.length;
        }

        @Override
        public boolean hasNext(){
            return (position + 1) < data.length;
        }
        @Override
        public boolean next(){
            if (++position == data.length) {
                reset();
                return false;
            } else return true;
        }
        @Override
        public boolean previous(){
            if (--position < 0){
                position = data.length-1;
                return false;
            } else return true;
        }

        @Override
        public Iterator4d copy() {
            return new Iterator4dV(data);
        }
    }

    /**
     *
     */
    class Iterator4dA extends Iterator3dA implements Iterator4d {
        private double[] w;

        Iterator4dA(double[] x, double[] y, double[] z, double[] w) {
            super(x, y, z);
            this.w = w;
        }

        @Override
        public double w() {
            return w[position];
        }
    }

    /**
     *
     */
    class Iterator4dR extends Iterator3dR implements Iterator4d {
        private double[] w;
        Iterator4dR(double x1, double x2, double[] y, double[] z, double[] w) {
            super(x1, x2, y, z);
            this.w = w;
        }

        @Override
        public double w() {
            return w[position];
        }
    }
}
