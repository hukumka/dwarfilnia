package nktl.GL4;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Zheka Grushevskiy, NAKATEEL, 08.08.2016.
 */
public class ZRender implements GLEventListener {
    protected Map<String, ZShader> progs = new HashMap<>();
    protected int width, height;
    protected double timeDelta; // Время между кадрами в секундах.
    protected long oldTime, newTime; // Время в наносекундах

    protected ZCam cam = null;
    private ZAction action = null;

    public ZRender(){}

    @Override
    public void init(GLAutoDrawable glad) {
        System.out.println("Render.Init");
        // Инфа о графене
        ZShader.printGLInfo(glad.getGL().getGL4());
        oldTime = System.nanoTime();
    }

    @Override
    public void display(GLAutoDrawable glad) {
        newTime = System.nanoTime();
        recountTimeDelta();
        if (action != null) action.process(timeDelta);
    }

    @Override
    public void dispose(GLAutoDrawable glad) {
        GL4 gl = toGL(glad);
        System.out.println("Render.Dispose");
        Iterator<Map.Entry<String, ZShader>> it = progs.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String, ZShader> entry = it.next();
            try {
                entry.getValue().use(gl);
            } catch (ZShader.ZShaderException e) {
                e.printStackTrace();
            }
            entry.getValue().dispose(gl);
            it.remove();
        }
    }

    @Override
    public void reshape(GLAutoDrawable glad, int x, int y, int width, int height) {
        GL4 gl = toGL(glad);
        gl.glViewport(x, y, width, height); // Изменение размеров холста (что-то вроде)
        this.width = width;
        this.height = height;
        float res = (float)width/height;

        if (cam != null) {
            cam.setResolution(res);
            this.cam.recountProjM();
        }
    }

    public void setCamera(ZCam cam){
        this.cam = cam;
    }

    public void setAction(ZAction action){
        this.action = action;
    }

    private void recountTimeDelta(){
        timeDelta = (newTime - oldTime)/1e9; // В секундах
        oldTime = newTime;
    }

    protected GL4 toGL(GLAutoDrawable glad){
        return glad.getGL().getGL4();
    }
}
