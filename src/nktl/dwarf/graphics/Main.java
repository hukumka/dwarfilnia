package nktl.dwarf.graphics;

import nktl.GL4.ZCam;
import nktl.GL4.ZPanel;
import nktl.GL4.ZRender;
import nktl.dwarf.graphics.controls.InputTest;
import nktl.dwarf.graphics.controls.TempKeyProcessor;
import nktl.dwarf.graphics.controls.ZActionContainer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

/**
 * Created by Zheka Grushevskiy, NAKATEEL, 30.11.2016.
 */
public class Main {

    private static JFrame mainFrame = null;
    private static ZPanel mainPanel = null;

    public static void main(String[]args){
        SwingUtilities.invokeLater(Main::init);
    }

    private static void init(){

        ZCam camera = new ZCam();
        camera.setPosition(0f, 0f, 0f);
        camera.setDir(-1, -1, -1);
        camera.setUp(0, 1, 0);


        ZRender render = new Render();
        render.setCamera(camera);

        mainPanel = new ZPanel(render, 60, false);

        // Настройка панели
        mainFrame = new JFrame("GraphEn GL4 Demo");
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setSize(1280, 720);
        mainFrame.setResizable(true);
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                disposeAll();
                System.exit(0);
            }
        });

        // Добавление контента
        mainFrame.setContentPane(mainPanel);

        // ДОбавление управления
        ZActionContainer actions = new ZActionContainer();
        render.setAction(actions);
        InputTest.initInput(mainPanel, actions, camera);


        Cursor
                transparentCursor = createTransparentCursor(),
                normalCursor = mainFrame.getCursor();
        mainPanel.addCanvasKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                boolean modeSwitched = false;
                switch (e.getKeyCode()){
                    case KeyEvent.VK_F4:
                        ZActionContainer.switchMode();
                        modeSwitched = true;
                        break;
                    case KeyEvent.VK_ESCAPE:
                        ZActionContainer.switchModePAC();
                        modeSwitched = true;
                        break;
                    case KeyEvent.VK_CLOSE_BRACKET:
                        TempKeyProcessor.moveSpeedUp();
                        break;
                    case KeyEvent.VK_OPEN_BRACKET:
                        TempKeyProcessor.moveSpeedLow();
                        break;
                }
                if (modeSwitched) mainFrame.setCursor(
                        ZActionContainer.getCurrentMode() == ZActionContainer.Mode.POINT_AND_CLICK ?
                                normalCursor : transparentCursor
                );
            }
        });


        // Включение
        mainFrame.setVisible(true);
        mainPanel.startAnimator();
    }

    private static void disposeAll(){
        if (mainPanel != null) mainPanel.dispose();
        if (mainFrame != null) mainFrame.dispose();
    }

    private static Cursor createTransparentCursor(){
        BufferedImage bi = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        return Toolkit.getDefaultToolkit()
                .createCustomCursor(bi, new Point(0, 0), "transparentCursor");
    }
}
