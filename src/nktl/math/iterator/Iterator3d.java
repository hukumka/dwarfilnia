package nktl.math.iterator;

import nktl.math.geom.Vec3d;

/**
 * Created by Zheka Grushevskiy, NAKATEEL, 04.09.2017.
 */
public interface Iterator3d extends Iterator2d {
    double z();

    /*
        Генераторы:
     */
    @Deprecated static Iterator3dV getIterator(Vec3d... data){
        return over(data);
    }
    @Deprecated static Iterator3dA getIterator(double[] x, double[] y, double[] z){
        return over(x, y, z);
    }
    @Deprecated static Iterator3dR getIterator(double x_start, double x_stop, double[] y, double[] z){
        return over(x_start, x_stop, y, z);
    }

    static Iterator3dV over(Vec3d... data){
        if (data == null || data.length == 0)
            return null;
        return new Iterator3dV(data);
    }

    static Iterator3dA over(double[] x, double[] y, double[] z){
        if (x == null || y == null || z == null ||
                x.length != y.length || x.length != z.length ||
                x.length == 0)
            return null;
        return new Iterator3dA(x, y, z);
    }

    static Iterator3dR over(double x_start, double x_stop, double[] y, double[] z){
        if (x_start == x_stop ||
            y == null || y.length == 0 ||
            z == null || z.length == 0)
            return null;
        return new Iterator3dR(x_start, x_stop, y, z);
    }



    /*
        РЕАЛИЗАЦИИ ИТЕРАТОРОВ
     */

    /**
     * Итератор по диапазону
     * Created by Zheka Grushevskiy, NAKATEEL, 04.09.2017.
     */
    class Iterator3dR extends Iterator2dR implements Iterator3d{
        private double[] z;

        Iterator3dR(double x1, double x2, double[] y, double [] z) {
            super(x1, x2, y);
            this.z = z;
        }

        @Override
        public double z() {
            return z[position];
        }
    }

    /**
     * Итератор по массиву векторов
     * Created by Zheka Grushevskiy, NAKATEEL, 04.09.2017.
     */
    class Iterator3dA extends Iterator2dA implements Iterator3d{
        private double[] z;

        Iterator3dA(double[] x, double[] y, double[] z) {
            super(x, y);
            this.z = z;
        }

        @Override
        public double z() {
            return z[position];
        }
    }

    /**
     * Итератор по массиву векторов
     * Created by Zheka Grushevskiy, NAKATEEL, 04.09.2017.
     */
    class Iterator3dV extends AbsIterator implements Iterator3d {
        private Vec3d[] data;

        Iterator3dV(Vec3d[] data){
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
        public Iterator2d copy() {
            return new Iterator3dV(data);
        }
    }
}



