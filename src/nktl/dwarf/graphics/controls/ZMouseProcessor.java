package nktl.dwarf.graphics.controls;

import nktl.GL4.ZAction;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Zheka Grushevskiy, NAKATEEL, 28.11.2015.
 */
public abstract class ZMouseProcessor extends MouseAdapter implements ZAction {
    protected boolean mouseInWindow = false;
    protected double deltaTime;

    @Override
    public void mouseEntered(MouseEvent e) {
        mouseInWindow = true;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        mouseInWindow = false;
    }

    @Override
    public void process(double deltaTime) {
        this.deltaTime = deltaTime;
        process();
    }
}
