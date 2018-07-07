package nktl.dwarf.graphics.controls;

import nktl.GL4.ZEyeInterface;

/**
 * Класс тактируемого перемещения про пространству.
 * Ну в том плане что при запуске основного метода в keyProcessor
 * передается время между двумя соседними вызовами отрисовщика изображения.
 * Это делается для того чтобы при падении фпс скорость перемещения не изменялась.
 *
 * Класс можно считать завершенным, т.к. предназначен исключительно для перемещения по сцене.
 * Однако стоит переместить в него несколько методов из класса UniformContainer, чтобы
 * последний больше напоминал простой контейнер векторов и матриц, а KeyProcessor полностью
 * обрабатывал перемещение.
 *
 * Created by Zheka Grushevskiy, NAKATEEL, 27.11.2015.
 */
public class TempKeyProcessor extends ZKeyProcessor{
    // Константы
    // Множители для векторов перемещения, если перемещение
    // одновременно происходит по 2-м или 3-м осям
    private static final double m2d = Math.pow(2, 0.5)/2;
    private static final double m3d = 1.0/Math.pow(3, 0.5);

    // Скорость перемещения. Потом сделаю чтобы загружалась с ini-шника и могла меняться
    private static final float
            minSpeed = 0.005f,
            step = 0.001f,
            maxSpeed = minSpeed + 45 * step;

    private static float moveSpeed1d, moveSpeed2d, moveSpeed3d;
    private static final float rollSpeed = 0.002f;

    private ZEyeInterface camera; // Переменная UniformContainer с векторами. См. nktl.GL3.render.UniformContainer



    public TempKeyProcessor(ZKeyboardHandler handler, ZActionMap actionMap, ZEyeInterface camera) {
        super(handler, actionMap);
        recountMoveSpeed(minSpeed);
        this.camera = camera;
    }

    public static void moveSpeedUp(){
        if (moveSpeed1d < maxSpeed) recountMoveSpeed(moveSpeed1d + step);
    }

    public static void moveSpeedLow(){
        if (moveSpeed1d > minSpeed) recountMoveSpeed(moveSpeed1d - step);
    }

    private static void recountMoveSpeed(float speed){
        moveSpeed1d = speed;
        moveSpeed2d = (float) (moveSpeed1d * m2d);
        moveSpeed3d = (float) (moveSpeed1d * m3d);
    }

    @Override
    public void process() {
        switch (ZActionContainer.getCurrentMode()){
            case FPS_MODE:
                // Проверяем статусы клавиш
                boolean forward = handler.getState(actionMap.getKeyCode("moveForward"));
                boolean backward = handler.getState(actionMap.getKeyCode("moveBackward"));
                boolean left = handler.getState(actionMap.getKeyCode("moveLeft"));
                boolean right = handler.getState(actionMap.getKeyCode("moveRight"));
                boolean up = handler.getState(actionMap.getKeyCode("moveUp"));
                boolean down = handler.getState(actionMap.getKeyCode("moveDown"));
                boolean rollLeft = handler.getState(actionMap.getKeyCode("rollLeft"));
                boolean rollRight = handler.getState(actionMap.getKeyCode("rollRight"));

                // Обработка перемещения. Перемещение состоится только если нажала тишь одна клавиша,
                // отвечающая за перемещение по сцене
                int doDir = (int)(forward? (backward? 0:deltaTime) : backward?-deltaTime:0);
                int doUp = (int)(up? (down? 0:deltaTime) : down?-deltaTime:0);
                int doRight = (int)(right? (left? 0:deltaTime) : left?-deltaTime:0);

                // Рассчет скоростей
                float dirSpeed, upSpeed, rightSpeed;
                if (doDir != 0 && doUp != 0 && doRight != 0){
                    dirSpeed = moveSpeed3d * doDir;
                    upSpeed = moveSpeed3d * doUp;
                    rightSpeed = moveSpeed3d * doRight;
                } else if ((doDir != 0 && doUp != 0) || (doUp != 0 && doRight != 0) || (doDir != 0 && doRight != 0)){
                    dirSpeed = moveSpeed2d * doDir;
                    upSpeed = moveSpeed2d * doUp;
                    rightSpeed = moveSpeed2d * doRight;
                } else {
                    dirSpeed = moveSpeed1d * doDir;
                    upSpeed = moveSpeed1d * doUp;
                    rightSpeed = moveSpeed1d * doRight;
                }

                // Обработка вращения
                int roll = (int) (rollRight ? (rollLeft?0:deltaTime) : rollLeft ? -deltaTime : 0);
                // Вращение векторов.
                camera.rotateRoll(roll*rollSpeed);
                camera.move(dirSpeed, upSpeed, rightSpeed);
                break;

            case POINT_AND_CLICK:
                // В этом режиме ActionContainer'a перемещение не будет осуществляться мышью. Потом.
                break;
        }
    }
}
