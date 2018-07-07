package nktl.dwarf.graphics.controls;

import nktl.GL4.ZEyeInterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Класс для обработки событий мыши.
 * На данный момент обрабатывает только вращение камерой.
 *
 * Created by Zheka Grushevskiy, NAKATEEL, 28.11.2015.
 */
public class TempMouseProcessor extends ZMouseProcessor {

    private JPanel panel; // панель в пределах которой надо перемещать мышь
    private static final float mouseSpeed = 0.0001f; // Скорость вращения
    private Robot robot; // робот для перемещения курсора мыши в центр.

    private ZEyeInterface camera; // Контейнер матриц и векторов

    // Конструктор
    public TempMouseProcessor(JPanel relativePanel, ZEyeInterface camera){
        this.panel = relativePanel;
        this.camera = camera;
        try {
            this.robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        super.mouseExited(e);
        //ZActionContainer.switchModePAC();
    }

    @Override
    public void process() {
        switch (ZActionContainer.getCurrentMode()){
            case FPS_MODE:
                if (mouseInWindow){
                    // Находим расположение мыши в окне и окна в системе
                    Point winPos = panel.getLocationOnScreen();
                    Point cp = panel.getMousePosition();

                    if (cp != null) {
                        // Находим центр окна
                        int widthD2 = panel.getWidth()/2;
                        int heightD2 = panel.getHeight()/2;
                        // Вычисляем поворот через расстояние от курсора до центра
                        float deltaYaw = (float) deltaTime * (widthD2 - cp.x);
                        float deltaPitch = (float) deltaTime * (heightD2 - cp.y);
                        // Перемещаем мышь обратно в центр
                        robot.mouseMove(winPos.x + widthD2, winPos.y + heightD2);
                        // Поворачиваем
                        camera.rotatePitchNYaw(mouseSpeed, deltaPitch, deltaYaw);
                    }
                }
                break;
            case POINT_AND_CLICK:

                break;
        }
    }
}
