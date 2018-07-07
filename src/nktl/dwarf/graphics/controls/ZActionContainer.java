package nktl.dwarf.graphics.controls;

import nktl.GL4.ZAction;

import java.util.HashSet;

/**
 * Created by Zheka Grushevskiy, NAKATEEL, 26.11.2015.
 */
public class ZActionContainer implements ZAction {
    public enum Mode{
        POINT_AND_CLICK, FPS_MODE
    }
    private static Mode currentMode = Mode.POINT_AND_CLICK;

    private long lastTime = System.nanoTime(), newTime;
    private double deltaTime;
    HashSet<ZAction> aps = new HashSet<>();

    @Override
    public void process(double timeDelta) {
        process();
    }

    @Override
    public void process() {
        recountDelta();
        synchronized (aps){
            for (ZAction ap : aps){
                ap.process(deltaTime);
            }
        }

    }

    public void addActionProcessor(ZAction ap){
        if (!aps.contains(ap)){
            aps.add(ap);
        }
    }

    public void removeActionProcessor(ZAction ap){
        if (aps.contains(ap)){
            aps.remove(ap);
        }
    }

    public static void switchModeFPS(){
        currentMode = Mode.FPS_MODE;

    }

    public static void switchModePAC(){
        currentMode = Mode.POINT_AND_CLICK;

    }

    public static void switchMode() {
        if (currentMode == Mode.FPS_MODE){
            switchModePAC();
        } else switchModeFPS();
    }

    public static Mode getCurrentMode(){
        return currentMode;
    }

    public void recountDelta(){
        newTime = System.nanoTime();
        deltaTime = (newTime - lastTime)/1e6;
        lastTime = newTime;
    }

    public double getDeltaTime(){
        return deltaTime;
    }
}
