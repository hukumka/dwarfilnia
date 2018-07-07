package nktl.dwarf.graphics.controls;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

/**
 * Created by Zheka Grushevskiy, NAKATEEL, 26.11.2015.
 */
public class ZKeyboardHandler implements KeyListener {
    HashMap<Integer, KeyState> keySet = new HashMap<>();


    @Override
    public void keyTyped(KeyEvent e) {
        // Nope
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (keySet.containsKey(e.getKeyCode())) {
            if (keySet.containsKey(e.getKeyCode())) keySet.get(e.getKeyCode()).pressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (keySet.containsKey(e.getKeyCode())) {
            if (keySet.containsKey(e.getKeyCode())) keySet.get(e.getKeyCode()).pressed = false;
        }
    }

    public static class KeyState{
        protected boolean pressed;
        public boolean isPressed(){
            return pressed;
        }
    }

    public boolean getState(int keyCode){
        return keySet.containsKey(keyCode) && keySet.get(keyCode).isPressed();
    }

    public void addKey(int keyCode){
        keySet.put(keyCode, new KeyState());
    }

    public void removeKey(int keyCode){
        keySet.remove(keyCode);
    }
}
