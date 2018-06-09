package nktl.math.iterator;

import nktl.math.geom.Vec2d;

/**
 * Created by Zheka Grushevskiy, NAKATEEL, 01.02.2017.
 */
public interface Iterator2d extends Iterator {

    /**
     * Возвращает значения x и y элемента
     */
    double x();
    double y();



    /*
        Генераторы:
     */
    @Deprecated static Iterator2dV getIterator(Vec2d... data){
        return over(data);
    }
    @Deprecated static Iterator2dA getIterator(double[] x, double[] y){
        return over(x, y);
    }
    @Deprecated static Iterator2dR getIterator(double x_start, double x_stop, double[] y){
        return over(x_start, x_stop, y);
    }


    static Iterator2dV over(Vec2d... data){
        if (data == null || data.length == 0)
            return null;
        return new Iterator2dV(data);
    }

    static Iterator2dA over(double[] x, double[] y){
        if (x == null || y == null || x.length != y.length || x.length == 0)
            return null;
        return new Iterator2dA(x, y);
    }

    static Iterator2dR over(double x_start, double x_stop, double[] y){
        if (x_start == x_stop ||
            y == null || y.length == 0)
            return null;
        return new Iterator2dR(x_start, x_stop, y);
    }

    /*
        РЕАЗИЛАЦИИ ИТЕРАТОРОВ
     */

    /*
        ИТЕРАТОР ПО ДИАПАЗОНУ
        Created by Zheka Grushevskiy, NAKATEEL, 18.08.2017.
    */
    class Iterator2dR extends AbsIterator implements Iterator2d{

        private double x0, y[], step;

        private Iterator2dR(double x0, double[] y, double step){
            this.x0 = x0;
            this.y = y;
            this.step = step;
        }

        Iterator2dR(double x1, double x2, double[] y){
            double xStart, xStop;
            if (x1 <= x2) {
                xStart = x1;
                xStop = x2;
            } else {
                xStart = x2;
                xStop = x1;
            }
            this.y = y;
            step = (xStop - xStart)/(y.length-1);
            x0 = xStart;
        }

        @Override
        public void last() {
            position = y.length-1;
        }

        @Override
        public double x(){
            return this.x0 + step*position;
        }
        @Override
        public double y(){
            return this.y[position];
        }
        @Override
        public int length() {
            return y.length;
        }

        @Override
        public boolean hasNext(){
            return (position + 1) < y.length;
        }
        @Override
        public boolean next(){
            if (++position == y.length) {
                reset();
                return false;
            } else return true;
        }
        @Override
        public boolean previous(){
            if (--position < 0){
                position = y.length-1;
                return false;
            } else return true;
        }

        @Override
        public Iterator2d copy() {
            return new Iterator2dR(x0, y, step);
        }
    }

    /*
        ИТЕРАТОР ПО МАССИВАМ
        Created by Zheka Grushevskiy, NAKATEEL, 01.02.2017.
     */
    class Iterator2dA extends AbsIterator implements Iterator2d{
        private double[] x, y;

        Iterator2dA(double[] x, double[] y){
            this.x = x;
            this.y = y;
        }

        @Override
        public void last() {
            position = x.length-1;
        }

        @Override
        public int length() {
            return y.length;
        }

        @Override
        public double x(){
            return this.x[position];
        }
        @Override
        public double y(){
            return this.y[position];
        }
        @Override
        public boolean hasNext(){
            return (position + 1) < x.length;
        }
        @Override
        public boolean next(){
            if (++position == x.length) {
                reset();
                return false;
            } else return true;
        }
        @Override
        public boolean previous(){
            if (--position < 0){
                position = x.length-1;
                return false;
            } else return true;
        }

        @Override
        public Iterator2d copy() {
            return new Iterator2dA(x, y);
        }
    }

    /**
     * Итератор мо массиву векторов
     * Created by Zheka Grushevskiy, NAKATEEL, 01.02.2017.
     */
    //public
    class Iterator2dV extends AbsIterator implements Iterator2d {
        private Vec2d[] data;

        Iterator2dV(Vec2d[] data){
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
            return new Iterator2dV(data);
        }
    }
}

