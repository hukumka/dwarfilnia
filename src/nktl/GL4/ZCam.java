package nktl.GL4;

import nktl.GL4.util.Mat3f;
import nktl.GL4.util.Mat4f;
import nktl.GL4.util.Vec3f;

/**
 * Created by Zheka Grushevskiy, NAKATEEL, 26.08.2016.
 */
public class ZCam implements ZEyeInterface {
    public final Vec3f
            position = new Vec3f(0, 0, 0),
            dir = new Vec3f(0, 0, -1),
            up = new Vec3f(0, 1, 0),
            right = new Vec3f(1, 0, 0);
    public final Mat4f
            PVM = Mat4f.genIden(),
            lookAtM = Mat4f.genIden(),
            projM = Mat4f.genIden();
    public final Mat3f
            normM = new Mat3f();

    private float fov = 60, near = 0.1f, far = 1000f, resolution = 1f;

    // Поворот и перемещение по векторам

    // Сначала вращение
    /**
     * Методы для вращения. ВНИМАНИЕ! Кароче. Пересчитывание вектора right
     * помещено в коментарии, т.к. достаточно пересчитать вектора dir и up.
     * Вектор right потом пересчитается в {@link #recountLookAtM}.
     * Такие дела.
     */
    @Override public void rotateRoll(float alpha){
        float c = (float) Math.cos(alpha);
        float s = (float) Math.sin(alpha);

        //float rX = right[0]*c - up[0]*s;
        //float rY = right[1]*c - up[1]*s;
        //float rZ = right[2]*c - up[2]*s;

        float uX = right.x*s + up.x*c;
        float uY = right.y*s + up.y*c;
        float uZ = right.z*s + up.z*c;
        up.x = uX; // right[0] = rX;
        up.y = uY; // right[1] = rY;
        up.z = uZ; // right[2] = rZ;
    }

    /**
     *
     * @param alpha = скорость
     * @param x = pitch
     * @param y = yaw
     */
    @Override public void rotatePitchNYaw(float alpha, float x, float y){
        float c = (float) Math.cos(alpha);
        float s = (float) Math.sin(alpha);
        float nc = 1f - c;

        //float xync = x * y * nc;

        //float rX = (x*x*nc + c)*right[0] + xync*up[0] + y*s*dir[0];

        float xync = x * y * nc, yyncc = y*y*nc + c, xs = x*s, ys = y*s;

        float uX = xync*right.x + yyncc*up.x - xs* dir.x;
        float uY = xync*right.y + yyncc*up.y - xs* dir.y;
        float uZ = xync*right.z + yyncc*up.z - xs* dir.z;

        float dX = -ys*right.x + xs*up.x + c* dir.x;
        float dY = -ys*right.y + xs*up.y + c* dir.y;
        float dZ = -ys*right.z + xs*up.z + c* dir.z;

        up.x = uX; dir.x = dX;
        up.y = uY; dir.y = dY;
        up.z = uZ; dir.z = dZ;
    }

    // Затем перемещение
    private void move(Vec3f direction, float speed){
        position.x += direction.x*speed;
        position.y += direction.y*speed;
        position.z += direction.z*speed;
    }

    public void moveForward(float speed){ move(dir, speed); }

    public void moveUp(float speed){ move(up, speed); }

    public void moveRight(float speed){ move(right, speed); }

    @Override public void move(float speedForward, float speedUp, float speedRight){
        position.x += dir.x*speedForward + up.x*speedUp + right.x*speedRight;
        position.y += dir.y*speedForward + up.y*speedUp + right.y*speedRight;
        position.z += dir.z*speedForward + up.z*speedUp + right.z*speedRight;
    }

    // насильное перемещение/вращение
    @Override public void setPosition(float x, float y, float z){
        position.x = x;
        position.y = y;
        position.z = z;
    }

    @Override public void setDir(float x, float y, float z){
        dir.x = x;
        dir.y = y;
        dir.z = z;
    }

    @Override public void setUp(float x, float y, float z){
        up.x = x;
        up.y = y;
        up.z = z;
    }

    @Override
    public void lookAtPoint(float x, float y, float z) {
        dir.x = x - position.x;
        dir.y = y - position.y;
        dir.z = z - position.z;
    }

    // Пересчет матриц
    public Mat4f recountLookAtM(){
        normM.copyFrom(lookAtM.setLookAt(position, dir, up, right));
        return lookAtM;
    }

    public Mat4f recountProjM(){
        return projM.setPerspective(fov, resolution, near, far);
    }

    public Mat4f recountPVM(){
        return PVM.multiMMIn(projM, lookAtM);
    }

    private static final Mat4f tempM = new Mat4f();
    public Mat4f recountPVM(Mat4f modelM){
        synchronized (tempM){
            return PVM.multiMMIn(projM, tempM.multiMMIn(lookAtM, modelM));
        }
    }
    public Mat4f recountPVM_mv(Mat4f mv){
        return PVM.multiMMIn(projM, mv);
    }

    // Изменение параметров камеры
    public void setFOV(float newFow){
        this.fov = newFow;
    }

    public void setResolution(float resolution){
        this.resolution = resolution;
    }

    public void setNearNFar(float near, float far){
        this.near = near;
        this.far = far;
    }

    public void setFOVNResolution(float fov, float resolution){
        this.fov = fov;
        this.resolution = resolution;
    }
}
