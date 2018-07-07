package nktl.GL4;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

/**
 * Created by Zheka Grushevskiy, NAKATEEL, 09.08.2016.
 */
public class ZPanel extends JPanel {

    private GLCanvas canvas;
    private FPSAnimator animator;

    public ZPanel(ZRender renderer, int maxFps, boolean fixedRate){

        this.canvas = new GLCanvas(new GLCapabilities(GLProfile.get(GLProfile.GL4)));
        this.canvas.addGLEventListener(renderer);

        this.animator = new FPSAnimator(canvas, maxFps, fixedRate);

        this.setLayout(new BorderLayout());
        this.add(canvas, BorderLayout.CENTER);
    }

    public void startAnimator(){
        this.animator.start();
    }

    public void stopAnimator(){
        if (this.animator.isStarted()){
            this.animator.stop();
        }
    }

    public void dispose(){
        stopAnimator();
        canvas.destroy();
    }

    public void addCanvasKeyListener(KeyListener keyListener){
        this.canvas.addKeyListener(keyListener);
    }

    public void removeCanvasKeyListener(KeyListener keyListener){
        this.canvas.removeKeyListener(keyListener);
    }

    public void addCanvasMouseListener(MouseListener mouseListener){
        this.canvas.addMouseListener(mouseListener);
    }

    public void removeCanvasMouseListener(MouseListener mouseListener){
        this.canvas.removeMouseListener(mouseListener);
    }
}
