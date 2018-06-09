package nktl.math;

/**
 * Класс для комплексных чисел.
 * -------------------------------------------------------
 * Конструкторы:
 *      {@link #Cnum()}
 *      {@link #Cnum(Cnum)}
 *      {@link #Cnum(double, double)}
 *      {@link #Cnum(double)}
 *
 * -------------------------------------------------------
 * Копирование
 *      {@link #copy()}
 *      {@link #copy(Cnum)}
 *      {@link #copy(double, double)}
 *      {@link #copy(double)}
 *
 * -------------------------------------------------------
 * Сложение
 *      {@link #plus(Cnum)}     {@link #plusIn(Cnum)}
 *      {@link #plus(double, double)} {@link #plusIn(double, double)}
 *
 * -------------------------------------------------------
 * Вычитание
 *      {@link #minus(Cnum)}    {@link #minusIn(Cnum)}
 *
 * -------------------------------------------------------
 * Умножение
 *      {@link #mult(Cnum)}     {@link #multIn(Cnum)}
 *      {@link #mult(double)}   {@link #multIn(double)}
 *
 * -------------------------------------------------------
 * Деление
 *      {@link #div(Cnum)}      {@link #divIn(Cnum)}
 *
 * -------------------------------------------------------
 * Возведение в степень
 *      {@link #pow(double)}    {@link #powIn(double)}
 *
 * -------------------------------------------------------
 * Нахождение корней целой степени
 *      {@link #roots(int)}
 *
 * -------------------------------------------------------
 * Модуль
 *      {@link #abs()}      {@link #abs2()}
 *
 * -------------------------------------------------------
 * Аргумент
 *      {@link #arg()}      {@link #argD()}
 *
 * -------------------------------------------------------
 * Сопряженное
 *      {@link #conj()}
 *
 * -------------------------------------------------------
 * Обратное
 *      {@link #rec()}      {@link #recIn()}
 *
 * -------------------------------------------------------
 * Зеркальное/Отрицательное
 *      {@link #neg()}      {@link #negIn()}
 *
 * -------------------------------------------------------
 * Строка в алгебраической форме
 *      {@link #toString()}
 *
 * -------------------------------------------------------
 * Строка в показательной форме (через cis)
 *      {@link #polarString()}
 *
 * -------------------------------------------------------
 *
 * Created by Zheka Grushevskiy, NAKATEEL, 27.05.2017.
 */
public class Cnum {
    public double re, im;

    public Cnum(double re, double im){
        this.re = re;
        this.im = im;
    }

    public Cnum(double arg){
        copy(arg);
    }

    public Cnum(){
        this(0, 0);
    }

    public Cnum(Cnum c){
        this(c.re, c.im);
    }

    public Cnum copy(){
        return new Cnum(re, im);
    }

    public Cnum copy(Cnum c){
        return copy(c.re, c.im);
    }

    public Cnum copy(double re, double im){
        this.re = re;
        this.im = im;
        return this;
    }

    public Cnum copy(double arg){
        this.re = Math.cos(arg);
        this.im = Math.sin(arg);
        return this;
    }


    public Cnum plus(Cnum c){
        return new Cnum(this.re + c.re, this.im + c.im);
    }

    public Cnum plusIn(Cnum c){
        this.re += c.re;
        this.im += c.im;
        return this;
    }

    public Cnum plus(double re, double im) {
        return new Cnum(this.re + re, this.im + im);
    }

    public Cnum plusIn(double re, double im){
        this.re += re;
        this.im += im;
        return this;
    }

    public Cnum minus(Cnum c){
        return new Cnum(this.re - c.re, this.im - c.im);
    }

    public Cnum minusIn(Cnum c){
        this.re -= c.re;
        this.im -= c.im;
        return this;
    }

    public Cnum mult(Cnum c){
        return new Cnum(
                multRe(this.re, this.im, c.re, c.im),
                multIm(this.re, this.im, c.re, c.im));
    }

    public Cnum multIn(Cnum c){
        return copy(
                multRe(this.re, this.im, c.re, c.im),
                multIm(this.re, this.im, c.re, c.im));
    }

    public Cnum mult(double d){
        return new Cnum(re*d, im*d);
    }

    public Cnum multIn(double d){
        return copy(re*d, im*d);
    }

    public Cnum div(Cnum c){
        double a2 = c.abs2();
        return new Cnum(
                devRe(this.re, this.im, c.re, c.im)/a2,
                devIm(this.re, this.im, c.re, c.im)/a2);
    }

    public Cnum divIn(Cnum c){
        double a2 = c.abs2();
        return copy(
                devRe(this.re, this.im, c.re, c.im)/a2,
                devIm(this.re, this.im, c.re, c.im)/a2);
    }

    public Cnum pow(double power){
        double abs = Math.pow(abs(), power),
               arg = power*arg();
        return new Cnum(abs*Math.cos(arg), abs*Math.sin(arg));
    }

    public Cnum powIn(double power){
        double abs = Math.pow(abs(), power),
               arg = power*arg();
        return copy(abs*Math.cos(arg), abs*Math.sin(arg));
    }

    public Cnum[] roots(int power){
        if (power < 1) return new Cnum[]{pow(-power)};
        double abs = Math.pow(abs(), 1.0/power),
               arg = arg(), newArg, PI2 = 2*Math.PI;
        Cnum[] roots = new Cnum[power];
        for (int i = 0; i < power; i++) {
            newArg = (arg + PI2*i)/power;
            roots[i] = new Cnum(
                    abs*Math.cos(newArg),
                    abs*Math.sin(newArg));
        }
        return roots;
    }

    public double abs(){
        return Math.sqrt(abs2());
    }

    public double abs2(){
        return re*re + im*im;
    }

    public double arg(){
        return Math.atan2(im, re);
    }

    public double argD(){
        return arg()*180/Math.PI;
    }

    public Cnum conj(){
        return new Cnum(re, -im);
    }

    public Cnum rec(){
        double a2 = abs2();
        return new Cnum(re/a2, -im/a2);
    }

    public Cnum recIn(){
        double a2 = abs2();
        return copy(re/a2, -im/a2);
    }

    public Cnum neg(){
        return new Cnum(-re, -im);
    }

    public Cnum negIn(){
        return copy(-re, -im);
    }

    @Override
    public String toString() {
        if (im == 0) return Format.format(re) + "";
        if (re == 0) return (im > 0 ? "i" : "-i") + Format.format(Math.abs(im));
        if (im < 0) return Format.format(re) + " - i" + Format.format(-im);
        return Format.format(re) + " + i" + Format.format(im);
    }

    public String polarString() {
        return abs() + "*e^i(" + arg() + ')';
    }

    public static Cnum[] array(int length){
        Cnum[] array = new Cnum[length];
        for (int i = 0; i < length; i++)
            array[i] = new Cnum();
        return array;
    }

    /*
        PRIVATE-STATIC-СЕКЦИЯ
     */
    private static double multRe(double re1, double im1, double re2, double im2){
        return re1*re2 - im1*im2;
    }

    private static double multIm(double re1, double im1, double re2, double im2){
        return re1*im2 + im1*re2;
    }

    private static double devRe(double re1, double im1, double re2, double im2){
        return re1*re2 + im1*im2;
    }

    private static double devIm(double re1, double im1, double re2, double im2){
        return im1*re2 - re1*im2;
    }


}
