package nktl.dwarf.graphics.controls;

import nktl.GL4.ZAction;

/**
 * Created by Zheka Grushevskiy, NAKATEEL, 26.11.2015.
 */
public abstract class ZKeyProcessor implements ZAction {
    protected ZKeyboardHandler handler;
    protected ZActionMap actionMap;
    protected double deltaTime; // время между двумя кадрами в миллисекундах

    public ZKeyProcessor(ZKeyboardHandler handler, ZActionMap actionMap){
        this.handler = handler;
        this.actionMap = actionMap;
    }

    @Override
    public void process(double deltaTime) {
        this.deltaTime = deltaTime;
        process();
    }
}
