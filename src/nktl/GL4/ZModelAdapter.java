package nktl.GL4;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2ES2.GL_STREAM_DRAW;

/**
 * Created by Zheka Grushevskiy, NAKATEEL, 23.08.2016.
 */
public class ZModelAdapter {
    public enum ZDrawType {
        STATIC_DRAW(GL_STATIC_DRAW),
        DYNAMIC_DRAW(GL_DYNAMIC_DRAW),
        STREAM_DRAW(GL_STREAM_DRAW);

        private final int value;
        ZDrawType(int type){
            this.value = type;
        }
    }

    public static void setType(ZDrawType type){
        drawType = type;
    }

    private static ZDrawType drawType = ZDrawType.STATIC_DRAW;
    private static void createBuffer(GL4 gl, int bufferType, float[] array, int[] handle, int hOffset){
        createBuffer(gl, bufferType, handle, hOffset);
        gl.glBufferData(bufferType, array.length*Float.BYTES,
                Buffers.newDirectFloatBuffer(array), drawType.value);
    }

    private static void createBuffer(GL4 gl, int bufferType, int[] array, int[] handle, int hOffset){
        createBuffer(gl, bufferType, handle, hOffset);
        gl.glBufferData(bufferType, array.length*Integer.BYTES,
                Buffers.newDirectIntBuffer(array), drawType.value);
    }

    private static void createBuffer(GL4 gl, int bufferType, int[] handle, int hOffset){
        gl.glGenBuffers(1, handle, hOffset);
        gl.glBindBuffer(bufferType, handle[hOffset]);
    }

    private static void bufferDataI(GL4 gl, int bufferType, float[] array){
        gl.glBufferData(bufferType, array.length*Float.BYTES,
                Buffers.newDirectFloatBuffer(array), drawType.value);
    }

    public static void createVAO(GL4 gl, float[] array, int[] vao, int vaoOffset, int... strides){
        if (strides.length == 0) {
            System.err.println("Не указаны страйды.");
            return;
        }
        int[] handle = new int[1];

        gl.glGenVertexArrays(1, vao, vaoOffset);

        createBuffer(gl, GL_ARRAY_BUFFER, array, handle, 0);

        gl.glBindVertexArray(vao[vaoOffset]);

        int allStride = 0;
        for(int stride : strides) allStride += stride;

        for (int i = 0; i < strides.length; i++){
            gl.glEnableVertexAttribArray(i);
        }
        gl.glBindVertexBuffer(0, handle[0], 0, allStride*Float.BYTES);

        int stride = 0;
        for (int i = 0; i < strides.length; i++){
            gl.glVertexAttribFormat(i, strides[i], GL_FLOAT, false, stride*Float.BYTES);
            gl.glVertexAttribBinding(i, 0);
            stride+=strides[i];
        }
    }

    public static void addArrayToVAO(GL4 gl, float[] array, int attribNum, int stride, int[] vao, int vaoOffset, int bufferNum){
        if (vao[vaoOffset] == 0) {
            System.err.println("vao равен 0");
            return;
        }
        int[] handle = new int[1];
        createBuffer(gl, GL_ARRAY_BUFFER, array, handle, 0);

        gl.glBindVertexArray(vao[vaoOffset]);

        gl.glEnableVertexAttribArray(attribNum);
        gl.glBindVertexBuffer(bufferNum, handle[0], 0, stride * Float.BYTES);

        gl.glVertexAttribFormat(attribNum, stride, GL_FLOAT, false, 0);
        gl.glVertexAttribBinding(attribNum, bufferNum);
    }

    public static void addIndexesToVAO(GL4 gl, int[] ind, int[] vao, int vaoOffset){
        if (vao[vaoOffset] == 0) {
            System.err.println("vao равен 0");
            return;
        }

        gl.glBindVertexArray(vao[vaoOffset]);
        createBuffer(gl, GL_ELEMENT_ARRAY_BUFFER, ind, new int[1], 0);
    }
}
