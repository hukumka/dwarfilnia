package nktl.dwarf.graphics.controls;


import nktl.GL4.ZAction;
import nktl.GL4.ZEyeInterface;
import nktl.GL4.ZPanel;

import java.awt.event.KeyEvent;

/**
 * Created by Zheka Grushevskiy, NAKATEEL, 02.11.2015.
 */
public class InputTest {


    public static void initInput(ZPanel panel, ZActionContainer actions, ZEyeInterface camera){
        // Сначала с клавиатурой
        ZKeyboardHandler handler = new ZKeyboardHandler();
        ZActionMap map = new ZActionMap();
        TempKeyProcessor tkp = new TempKeyProcessor(handler, map, camera);
        actions.addActionProcessor(tkp);
        panel.addCanvasKeyListener(handler);

        handler.addKey(KeyEvent.VK_W);
        handler.addKey(KeyEvent.VK_S);
        handler.addKey(KeyEvent.VK_A);
        handler.addKey(KeyEvent.VK_D);
        handler.addKey(KeyEvent.VK_SPACE);
        handler.addKey(KeyEvent.VK_SHIFT);
        handler.addKey(KeyEvent.VK_Q);
        handler.addKey(KeyEvent.VK_E);

        map.addAction("moveForward" , KeyEvent.VK_W);
        map.addAction("moveBackward", KeyEvent.VK_S);
        map.addAction("moveLeft"    , KeyEvent.VK_A);
        map.addAction("moveRight"   , KeyEvent.VK_D);
        map.addAction("moveUp"      , KeyEvent.VK_SPACE);
        map.addAction("moveDown"    , KeyEvent.VK_SHIFT);
        map.addAction("rollLeft"    , KeyEvent.VK_Q);
        map.addAction("rollRight"   , KeyEvent.VK_E);

        // Потому с мышью
        TempMouseProcessor mouseProcessor = new TempMouseProcessor(panel, camera);
        actions.addActionProcessor(mouseProcessor);
        panel.addCanvasMouseListener(mouseProcessor);

        actions.addActionProcessor(new ZAction() {
            @Override
            public void process(double deltaTime) {
                process();
            }

            @Override
            public void process() {
                camera.recountLookAtM();
            }
        });
    }
}
