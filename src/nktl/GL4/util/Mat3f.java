package nktl.GL4.util;

/**
 *  Индексация:
 *  0  3  6
 *  1  4  7
 *  2  5  8
 *
 * Created by Zheka Grushevskiy, NAKATEEL, 26.08.2016.
 */
public class Mat3f extends ArrayHolder<Mat3f> {

    public Mat3f(){
        super(9);
    }
    public Mat3f(boolean iden){
        this();
        if (iden){
            arr[0] = arr[4] = arr[8] = 1;
            arr[1] = arr[2] = arr[3] =
            arr[5] = arr[6] = arr[7] = 0;
        }
    }

    public Mat3f(Mat4f mat4){
        this();
        float[] m4 = mat4.arr();
        mat4.copyTo(this);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++) {
                sb.append(arr[i + j * 3]);
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // ОПЕРАЦИИ НАД МАТРИЦАМИ
    // Копирование (дополнительные методы к ArrayHolder)
    public void copyFrom(Mat4f mat4){
        int offset = 0;
        for (int i = 0; i < 3; i++, offset++)
            for (int j = 0; j < 3; j++)
                this.arr[i*3+j] = mat4.arr()[offset++];
    }
}
