package nktl.GL4.util;

/**
 * Created by Zheka Grushevskiy, NAKATEEL, 29.08.2016.
 */
public class VAObject {
    public static final int vaoInd = 0; // Vertex Array Object
    public static final int nopInd = 1; // Number of points

    private final int[] values = new int[2];

    public VAObject(){}
    private VAObject (int vao, int numOfPoints){
        values[0] = vao;
        values[1] = numOfPoints;
    }

    public int getVAO(){
        return values[0];
    }

    public int getNOP(){
        return values[1];
    }

    public int[] getValues(){
        return values;
    }

    // Static
    public static VAObject createVAO(int vao, int nop){
        if (vao == 0 || nop == 0) return null;
        else return new VAObject(vao, nop);
    }
}
