package nktl.math;

/**
 * Created by Zheka Grushevskiy, NAKATEEL, 04.09.2017.
 */
public class RangeInt implements RangeD {
    private int min, max, span;

    /*
        Создание
     */
    public RangeInt(){}
    public RangeInt(int span){
        if (span >= 0) {
            this.max = this.span = span;
            this.min = 0;
        } else {
            this.min = span;
            this.max = 0;
            this.span = -span;
        }
    }
    public RangeInt(int min, int max) {
        this.set(min, max);
    }

    /*
        Геттеры
     */
    public int min(){ return min; }
    public int max(){ return max; }
    public int span(){ return span; }
    public int center() { return (min + max)/2; }

    @Override public double minD(){ return min; }
    @Override public double maxD(){ return max; }
    @Override public double spanD(){ return span; }
    @Override public double centerD(){ return (min + max)/2.; }

    /*
        Сеттеры
     */
    public void set(int min, int max){
        if (max < min) {
            this.min = max;
            this.max = min;
        } else {
            this.min = min;
            this.max = max;
        }
        recountDelta();
    }

    /*
        ОБНОВЛЕНИЕ ДАННЫХ
     */
    public void add(int value){
        boolean expanded;
        if (expanded = this.min > value)
            this.min = value;
        else if (expanded = this.max < value)
            this.max = value;
        if (expanded) recountDelta();
    }

    public void add(RangeInt r) {
        boolean expanded = false;
        if (r.max > this.max) {
            expanded = true;
            this.max = r.max;
        }

        if (r.min < this.min){
            expanded = true;
            this.min = r.min;
        }
        if (expanded) recountDelta();
    }

    /*
        КОПИРОВАНИЕ
     */
    public void copy(RangeInt r) {
        this.min = r.min;
        this.max = r.max;
        this.span = r.span;
    }

    public RangeInt copy(){
        return new RangeInt(this.min, this.max);
    }

    // Проверка на кратность
    public void multiplicity(int value, RangeInt dst){
        value = Math.abs(value);
        int maxDev = max/value;
        if (maxDev*value < max) maxDev+=1;

        int minDev = min/value;
        if (minDev*value > min) minDev-=1;
        dst.set(minDev*value, maxDev*value);
    }

    public void multiplicity(int value){
        multiplicity(value, this);
    }

    // Проверка на содержание
    public boolean has(int value){
        return value >= min && value <= max;
    }

    @Override
    public String toString(){
        return "RangeInt{" + min + " ... " + max + "}(" + span + ')';
    }

    private void recountDelta() {
        this.span = this.max - this.min;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RangeInt range = (RangeInt) o;

        return range.min == min &&
                range.max == max &&
                range.span == span;
    }


    /*
        Статические
     */
    public static double to01(int v, int min, int delta){
        return (v - min)/delta;
    }

    public static int from01(double v, int min, int delta){
        return (int) v*delta + min;
    }

    public static double to01(double v, RangeInt r){
        return (v - r.min)/r.span;
    }

    public static int from01(double v, RangeInt r){
        return (int) (v*r.span + r.min);
    }

    public static double to11(double v, RangeInt r){
        return (v - r.min)*2./r.span - 1.;
    }

    public static int from11(double v, RangeInt r){
        return (int) ((v+1.)*r.span/2. + r.min);
    }
}
