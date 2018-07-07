package nktl.GL4.util;

/**
 * Created by Zheka Grushevskiy, NAKATEEL, 26.08.2016.
 */
public abstract class ArrayHolder<T extends ArrayHolder> {
    public final float[] arr;

    protected ArrayHolder(int length) {
        arr = new float[length];
    }

    public float[] arr(){
        return arr;
    }

    public void copyFrom(T src){
        copyFrom(src.arr);
    }

    public void copyTo(T res){
        res.copyFrom(this);
    }

    protected void copyFrom(float[] src){
        System.arraycopy(src, 0, this.arr, 0, src.length);
    }
}
