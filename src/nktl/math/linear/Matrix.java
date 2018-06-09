package nktl.math.linear;

/**
 * Индексация:
 *  0  4   8  *
 *  1  5   9  *
 *  2  6  10  *
 *  *  *   *  *
 *
 * Created by Zheka Grushevskiy, NAKATEEL, 06.04.2018.
 */
public class Matrix {

    /*
        ОПРЕДЕЛИТЕЛЬ КВАДРАТНОЙ МАТРИЦЫ
     */
    public static int size(double[] src) {
        int size = (int) Math.round(Math.sqrt(src.length));
        if (size*size == src.length) return size;
        return -1;
    }

    // умножение матрицы на вектор
    public static void multMV(double[] dst, double[] src_m, double[]src_v, int src_size){
        for (int i = 0; i < src_size; i++) {
            dst[i] = 0;
            for (int j = i, k = 0; k < src_size; j+= src_size, ++k) {
                dst[i] += src_m[j]*src_v[k];
            }
        }
    }

    // определительно матрицы 2 на 2
    private static double det2x2(double[]src){
        return src[0]*src[3] - src[1]*src[2]; // норм
    }

    // определитель матрицы 3 на 3
    private static double det3x3(double[]src){
        return src[0] * (src[4]*src[8] - src[7]*src[5]) +
               src[3] * (src[7]*src[2] - src[1]*src[8]) +
               src[6] * (src[1]*src[5] - src[4]*src[2]); // норм
    }

    // определитель матрицы
    public static double det(double[]src) {
        int src_size = size(src);
        return det(src, src_size); // норм
    }

    // и это тоже определитель матрицы
    public static double det(double[]src, int src_size) {
        if (src_size == 2) return det2x2(src);
        double[] buff = smaller(src_size);
        double determinant = 0;
        for (int i = 0, mult = 1; i < src_size; i++, mult = -mult) {
            submat(buff, src, src_size, i, 0);
            determinant += src[i] * mult * det(buff, src_size-1);
        }
        return determinant; // норм
    }

    // Союзная матрица
    public static double[] adjunct(double[] src, int src_size){
        double[]dst = new double[src_size*src_size];
        adjunct(dst, src, src_size);
        return dst; // норм
    }
    // Союзная матрица
    private static void adjunct(double[] dst, double[] src, int src_size) {
        int buff_size = src_size - 1;
        double[]buff = new double[buff_size*buff_size];
        for (int i = 0; i < src.length; i++) {
            int col = i / src_size;
            int row = i - col*src_size;
            int sign = (int) Math.pow(-1, col+row);
            submat(buff, src, src_size, row, col);
            dst[row*src_size+col] = sign*det(buff, buff_size);
        } // норм
    }

    // минор один
    private static double minor(double[] buff, double[] src, int src_size, int row, int col) {
        submat(buff, src, src_size, row, col);
        return det(buff, src_size-1);
    }

    // Обратная
    public static double[] inverse(double[] src, int src_size) {
        double[] dst = new double[src_size*src_size];
        inverse(dst, src, src_size);
        return dst;
    }

    // Обратная
    public static void inverse(double[] dst, double[] src, int src_size) {
        adjunct(dst, src, src_size);

        double det = 0;
        for (int i = 0, j = 0; i < src_size; ++i, j += src_size){
            det += src[i] * dst[j];
        }
        System.out.println("src det = " + det);
        for (int i = 0; i < dst.length; i++)
            dst[i] /= det;
    }

    // Матрица Х на Х, где Х = src_size-1
    private static double[] smaller(int src_size){
        int dst_size = src_size - 1;
        return new double[dst_size*dst_size]; // норм, но нужно ли оно
    }

    // подматрица матрицы src, в которой вычеркивают row и  col
    public static double[] submat(double[]src, int src_size, int row, int col) {
        double[]dst = smaller(src_size);
        submat(dst, src, src_size, row, col);
        return dst; // норм
    }

    // подматрица
    public static void submat(double[] dst, double[]src, int src_size, int row, int col) {
        for (int s = 0, d = 0; s < src.length;) {
            int s_col = s / src_size;
            int s_row = s - s_col*src_size;
            if (s_col == col)
                s += src_size - s_row;
            else {
                if (s_row != row){
                    dst[d] = src[s];
                    ++d;
                }
                ++s;
            }
        } // норм
    }

    // Конвертация столбца и строки в индекс одномерного массива
    private static int num(int row, int col, int size) {
        return size * col + row;
    }

    public static void main(String[]args) {
        /*double[] src = {
                1, 2, 4,
                -3, 5, -2,
                4, -3, 7
        };

        System.out.println("det = " + det(src));

        double[] inv = inverse(src, size(src));

        printM(inv);/**/


        //*
        double[] src = {
                4, 3, 7, 8, 2,
                4, -1, 4, -3, -4,
                -6, 7, 8, 9, 1,
                4, 5, -1, -2, -4,
                5, -6, 5, 3, 1
        };

        double[] v1 = { 1, 1, 1, 1, 1 };
        double[] v2 = new double[5];

        int src_size = size(src);
        printM(src, src_size);
        double det = det(src, src_size);
        System.out.println(det+"\n");
        if (det == 0) {
            System.err.println("Determinant is zero.");
            return;
        }

        multMV(v2, src, v1, src_size);

        printV(v2);

        double[] inverse = inverse(src, src_size);
        printM(inverse, src_size);

        multMV(v1, inverse, v2, src_size);

        printV(v1);/**/

    }

    public static void printV(double[]src) {
        StringBuilder sb = new StringBuilder();
        sb.append("| ");
        for (double val : src) {
            sb.append(' ').append(val).append(" |");
        }
        sb.append('\n');
        System.out.println(sb.toString());
    }

    public static void printM(double[]src) {
        printM(src, size(src));
    }

    public static void printM(double[]src, int src_size){
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < src_size; row++) {
            sb.append("| ");
            for (int col = 0; col < src_size; col++, sb.append(" |")) {
                sb.append(' ').append(src[col*src_size + row]);
            }
            sb.append('\n');
        }
        System.out.println(sb.toString());
    }

}
