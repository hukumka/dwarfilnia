package nktl.math;

/**
 * Created by Zheka Grushevskiy, NAKATEEL, 27.07.2017.
 */
public class Range implements RangeD {
    private double min, max, span;

    /*
        Создание
     */
    public Range(){}
    public Range(double span){
        if (span >= 0) {
            this.max = this.span = span;
            this.min = 0;
        } else {
            this.min = span;
            this.max = 0;
            this.span = -span;
        }
    }
    public Range(double min, double max) {
        this.set(min, max);
    }

    /*
        Геттеры
     */
    public double min(){ return min; }
    public double max(){ return max; }
    public double span(){ return span; }
    public double center(){ return (min + max)/2; }

    @Override public double minD(){ return min; }
    @Override public double maxD(){ return max; }
    @Override public double spanD(){ return span; }
    @Override public double centerD(){ return (min + max)/2; }

    /*
        Сеттеры
     */
    public Range fromCenter(double center, double span){
        span/=2;
        set(center-span, center+span);
        return this;
    }

    public void set(double min, double max){
        if (max < min) {
            this.min = max;
            this.max = min;
        } else {
            this.min = min;
            this.max = max;
        }
        recountSpan();
    }

    /*
        ОБНОВЛЕНИЕ ДАННЫХ
     */
    public void add(double value){
        boolean expanded;
        if (expanded = this.min > value)
            this.min = value;
        else if (expanded = this.max < value)
            this.max = value;
        if (expanded) recountSpan();
    }

    public void add(Range r) {
        boolean expanded = false;
        if (r.max > this.max){
            this.max = r.max;
            expanded = true;
        }

        if (r.min < this.min){
            this.min = r.min;
            expanded = true;
        }
        if (expanded) recountSpan();
    }

    /*
        СДВИГ ДАННЫХ
     */
    public void shift(double shift){
        min += shift;
        max += shift;
    }

    /*
        КОПИРОВАНИЕ
     */
    public void copy(Range r) {
        this.min = r.min;
        this.max = r.max;
        this.span = r.span;
    }

    public Range copy(){
        return new Range(this.min, this.max);
    }

    public Range smoothCopy(){
        return makeSmooth(new Range());
    }

    public void smoothCopy(Range src){
        src.makeSmooth(this);
    }

    // Проверка на кратность
    public void multiplicity(double value, Range dst){
        value = Math.abs(value);
        int maxDev = (int) (max/value);
        if (maxDev*value < max) maxDev+=1;

        int minDev = (int) (min/value);
        if (minDev*value > min) minDev-=1;
        dst.set(minDev*value, maxDev*value);
    }

    public void multiplicity(double value){
        multiplicity(value, this);
    }

    // Проверка на содержание
    public boolean has(double value){
        return value >= min && value <= max;
    }

    @Override
    public String toString(){
        return "Range{" + Format.format(min) + " ... " + Format.format(max) + "}(" + Format.format(span) + ')';
                //String.format(Locale.US, format, min, max, span);
    }

    private void recountSpan() {
        this.span = this.max - this.min;
    }

    /*
        ПОРЯДКОВЫЙ МНОЖИТЕЛЬ (с добавлением)
     */
    private double getMultiplier(int addPositions){
        int power = span == 0 ? 0 : (int) Math.log10(span) - addPositions;
        return Math.pow(10, power);
    }

    private double getMultiplier(){
        return getMultiplier(2);
    }

    /*
        ОКРУГЛЕНИЕ
     */
    public Range makeSmooth(Range r){
        multiplicity(getMultiplier(), r);
        return r;
    }

    public Range makeSmooth(){
        multiplicity(getMultiplier());
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Range range = (Range) o;

        return range.min == min &&
               range.max == max &&
               range.span == span;
    }

    /*
        Статические
     */
    public static double to01(double v, double min, double delta){
        return (v - min)/delta;
    }

    public static double from01(double v, double min, double delta){
        return v*delta + min;
    }

    public static double to01(double v, Range r){
        return (v - r.min)/r.span;
    }

    public static double from01(double v, Range r){
        return v*r.span + r.min;
    }

    public static double to11(double v, Range r){
        return (v - r.min)*2./r.span - 1.;
    }

    public static double from11(double v, Range r){
        return (v+1.)*r.span/2. + r.min;
    }

    /*
        smin < v < smax
        0 < v - smin < smax - smin = sspan
        0 < (v - smin)/sspan < 1
        0 < (v - smin)*dspan/sspan < dspan = dmax - dmin
        dmin < dmin + (v - smin)*dspan/sspan < dmax
     */

    public static double map(double v, RangeD src, RangeD dst) {
        return dst.minD() + (v - src.minD())*dst.spanD()/src.spanD();
    }
}
