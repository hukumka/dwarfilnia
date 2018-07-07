package nktl.GL4;

/**
 * Created by Zheka Grushevskiy, NAKATEEL, 26.08.2016.
 */
public interface ZEyeInterface {
    // Поворот камеры
    void rotateRoll(float alpha);
    void rotatePitchNYaw(float alpha, float x, float y);

    // Перемещение камеры
    void move(float speedForward, float speedUp, float speedRight);

    // Позиционирование камеры
    void setPosition(float x, float y, float z);
    void setDir(float x, float y, float z);
    void setUp(float x, float y, float z);
    void lookAtPoint(float x, float y, float z);

    Object recountLookAtM();
}
