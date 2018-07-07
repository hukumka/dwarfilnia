package nktl.GL4.util;

/**
 * Индексация:
 *  0  4   8  12
 *  1  5   9  13
 *  2  6  10  14
 *  3  7  11  15
 *
 * Created by Zheka Grushevskiy, NAKATEEL, 26.08.2016.
 */
public class Mat4f extends ArrayHolder<Mat4f> {
    // Внимание. Темпы используются по всякому, потому не советуется что-либо с ними делать
    private final static float[]
            temp1 = new float[16],
            temp2 = new float[16];
    private final static double toRadFactor = Math.PI/180.0;


    public Mat4f(){
        super(16);
    }

    public Mat4f(Mat4f src){
        this();
        this.copyFrom(src);
    }

    public static float lengthV(float x, float y, float z){
        return (float) Math.sqrt(x*x + y*y + z*z);
    }

    // Вывод матрицы в консоль

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++) {
                sb.append(arr[i + j * 4]);
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }


    // ОПЕРАЦИИ НАД МАТРИЦАМИ
    // Копирование
    public void copyFrom(Mat3f mat3){
        int offset = 0;
        for (int i = 0; i < 3; i++, offset++)
            for (int j = 0; j < 3; j++)
                this.arr[offset++] = mat3.arr()[i*3+j];
    }

    public void copyTo(Mat3f mat3){
        mat3.copyFrom(this);
    }

    // Транспонирование матрицы
    public void transposeTo(Mat4f res) {
        transposeTo(res.arr());
    }

    public void transposeFrom(Mat4f src){
        src.transposeTo(this);
    }

    private void transposeTo(float[] res){
        for (int i = 0; i < 4; i++) {
            int base = i * 4;
            res[i]    = this.arr[base];
            res[i+4]  = this.arr[base+1];
            res[i+8]  = this.arr[base+2];
            res[i+12] = this.arr[base+3];
        }
    }

    public void transpose(){
        synchronized (temp1){
            transposeTo(temp1);
            copyFrom(temp1);
        }
    }

    // Перемножение матриц
    // res = m1 * m2
    private static void multiMM(float[] res, float[] m1, float[] m2){
        res[0]  = m1[0]*m2[0] + m1[4]*m2[1] + m1[ 8]*m2[2] + m1[12]*m2[3];
        res[1]  = m1[1]*m2[0] + m1[5]*m2[1] + m1[ 9]*m2[2] + m1[13]*m2[3];
        res[2]  = m1[2]*m2[0] + m1[6]*m2[1] + m1[10]*m2[2] + m1[14]*m2[3];
        res[3]  = m1[3]*m2[0] + m1[7]*m2[1] + m1[11]*m2[2] + m1[15]*m2[3];

        res[4]  = m1[0]*m2[4] + m1[4]*m2[5] + m1[ 8]*m2[6] + m1[12]*m2[7];
        res[5]  = m1[1]*m2[4] + m1[5]*m2[5] + m1[ 9]*m2[6] + m1[13]*m2[7];
        res[6]  = m1[2]*m2[4] + m1[6]*m2[5] + m1[10]*m2[6] + m1[14]*m2[7];
        res[7]  = m1[3]*m2[4] + m1[7]*m2[5] + m1[11]*m2[6] + m1[15]*m2[7];

        res[8]  = m1[0]*m2[8] + m1[4]*m2[9] + m1[ 8]*m2[10] + m1[12]*m2[11];
        res[9]  = m1[1]*m2[8] + m1[5]*m2[9] + m1[ 9]*m2[10] + m1[13]*m2[11];
        res[10] = m1[2]*m2[8] + m1[6]*m2[9] + m1[10]*m2[10] + m1[14]*m2[11];
        res[11] = m1[3]*m2[8] + m1[7]*m2[9] + m1[11]*m2[10] + m1[15]*m2[11];

        res[12] = m1[0]*m2[12] + m1[4]*m2[13] + m1[ 8]*m2[14] + m1[12]*m2[15];
        res[13] = m1[1]*m2[12] + m1[5]*m2[13] + m1[ 9]*m2[14] + m1[13]*m2[15];
        res[14] = m1[2]*m2[12] + m1[6]*m2[13] + m1[10]*m2[14] + m1[14]*m2[15];
        res[15] = m1[3]*m2[12] + m1[7]*m2[13] + m1[11]*m2[14] + m1[15]*m2[15];
    }

    // Вид: res = this * src
    public Mat4f multiMMTo(Mat4f res, Mat4f src){
        multiMM(res.arr(), this.arr(), src.arr());
        return res;
    }

    // Вид: this = this * src
    public Mat4f multiMMIn(Mat4f src){
        synchronized (temp1){
            multiMM(temp1, this.arr(), src.arr());
            copyFrom(temp1);
        }
        return this;
    }

    public Mat4f multiMMIn(Mat4f src1, Mat4f src2){
        multiMM(this.arr, src1.arr, src2.arr);
        return this;
    }

    public Mat4f multiMMNew(Mat4f src){
        return new Mat4f().multiMMIn(this, src);
    }

    // ТРАНСФОРМАЦИИ МАТРИЦ (перемещение, вращение и т.п.)

    // Перемещение матрицы в направлении {x y z}
    public void translate(float x, float y, float z){
        for(int i = 0; i < 4; i++){
            this.arr[12 + i] += this.arr[i]*x + this.arr[4+i]*y + this.arr[8+i]*z;
        }
    }

    // Перемещение матрицы в точку
    public void translateP(float x, float y, float z){
        for(int i = 0; i < 3; i++){
            this.arr[12 + i] = this.arr[i]*x + this.arr[4+i]*y + this.arr[8+i]*z;
        }
    }

    // Изменение размера матрицы src с сохранением результата в матрице res
    public void scale(float x, float y, float z) {
        for (int i = 0; i < 4; i++) {
            this.arr[i  ] *= x;
            this.arr[i+4] *= y;
            this.arr[i+8] *= z;
        }
    }

    public void scale(float xyz){
        this.scale(xyz, xyz, xyz);
    }

    // Поворот матрицы res на угол а по осям x y z
    public void rotate(float a, float x, float y, float z) {
        synchronized (temp1){
            crtRotM(temp1, a, x, y, z);
            multiMM(temp2, this.arr, temp1);
            System.arraycopy(temp2, 0, this.arr, 0, 16);
        }
    }

    // Поворот матрицы src на угол а по осям x y z с сохранением результата в res
    public void rotateTo(Mat4f res, float a, float x, float y, float z) {
        synchronized (temp1){
            crtRotM(temp1, a, x, y, z);
            multiMM(res.arr, this.arr, temp1);
        }
    }

    public void rotateFrom(Mat4f src, float a, float x, float y, float z){
        src.rotateTo(this, a, x, y, z);
    }

    // Создание поворотной матрицы из угла и множителей
    public void crtRotM(float a, float x, float y, float z){
        crtRotM(this.arr, a, x, y, z);
    }

    private static void crtRotM(float[] res, float a, float x, float y, float z){
        a*=toRadFactor;
        float s = (float) Math.sin(a);
        float c = (float) Math.cos(a);

        res[12] = 0;
        res[13] = 0;
        res[14] = 0;
        res[15] = 1; res[11] = 0; res[7] = 0; res[3] = 0;

        // Если требуется вращение только вокруг одной оси, то вращение описывается
        // простой матрицей поворота, которую можно в википедии найти
        if (x==1f && y==0f && z==0f) {
            res[0] = 1; res[4] = 0; res[8]  =  0;
            res[1] = 0; res[5] = c; res[9]  = -s;
            res[2] = 0; res[6] = s; res[10] =  c;
        } else if (x==0f && y==1f && z==0f) {
            res[0] =  c; res[4] = 0; res[8]  = s;
            res[1] =  0; res[5] = 1; res[9]  = 0;
            res[2] = -s; res[6] = 0; res[10] = c;
        } else if (x==0f && y==0f && z==1f) {
            res[0] = c; res[4] = -s; res[8]  = 0;
            res[1] = s; res[5] =  c; res[9]  = 0;
            res[2] = 0; res[6] =  0; res[10] = 1;
        } else {
            float len = lengthV(x, y, z);
            if (len != 1f) {
                float fix = 1f/len;
                x*= fix;
                y*= fix;
                z*= fix;
            }

            float nc = 1 - c;
            float xy = x * y; float xs = x * s;
            float yz = y * z; float ys = y * s;
            float zx = z * x; float zs = z * s;

            res[0] = x*x*nc + c; res[4] = xy*nc - zs; res[8]  = zx*nc + ys;
            res[1] = xy*nc + zs; res[5] = y*y*nc + c; res[9]  = yz*nc - xs;
            res[2] = zx*nc - ys; res[6] = yz*nc + xs; res[10] = z*z*nc + c;
        }
    }

    // Создание поворотной матрицы из углов Эйлера
    public void setEulerRotation(float xDeg, float yDeg, float zDeg) {
        xDeg *= toRadFactor;
        yDeg *= toRadFactor;
        zDeg *= toRadFactor;

        // Ищем синусы и косинусы
        float cx = (float) Math.cos(xDeg);
        float cy = (float) Math.cos(yDeg);
        float cz = (float) Math.cos(zDeg);

        float sx = (float) Math.sin(xDeg);
        float sy = (float) Math.sin(yDeg);
        float sz = (float) Math.sin(zDeg);

        float cxsy = cx * sy;
        float sxsy = sx * sy;

        this.arr[0]  =  cy * cz;
        this.arr[1]  = -cy * sz;
        this.arr[2]  = sy;
        this.arr[3]  = 0f;

        this.arr[4]  =  cxsy * cz + cx * sz;
        this.arr[5]  = -cxsy * sz + cx * cz;
        this.arr[6]  = -sx * cy;
        this.arr[7]  = 0f;

        this.arr[8]  = -sxsy * cz + sx * sz;
        this.arr[9]  =  sxsy * sz + sx * cz;
        this.arr[10] = cx * cy;
        this.arr[11] = 0f;

        this.arr[12] = 0f;
        this.arr[13] = 0f;
        this.arr[14] = 0f;
        this.arr[15] = 1f;
    }

    // Создание матрицы поворота по базису
    public void setBasisRotation(float xX, float xY, float xZ,
                             float yX, float yY, float yZ,
                             float zX, float zY, float zZ
    ){
        this.arr[0] = xX; this.arr[4] = yX; this.arr[8]  = zX; this.arr[12] = 0;
        this.arr[1] = xY; this.arr[5] = yY; this.arr[9]  = zY; this.arr[13] = 0;
        this.arr[2] = xZ; this.arr[6] = yZ; this.arr[10] = zZ; this.arr[14] = 0;
        this.arr[3] =  0; this.arr[7] =  0; this.arr[11] =  0; this.arr[15] = 1;
    }

    // МЕТОДЫ ДЛЯ ГЕНЕРАЦИИ МАТРИЦ

    // Создание матрицы ортогональной проекции
    public Mat4f setOrthogonal(float left, float right,
                               float bottom, float top,
                               float near, float far){
        if (left == right) {
            throw new IllegalArgumentException("left == right");
        }
        if (bottom == top) {
            throw new IllegalArgumentException("bottom == top");
        }
        if (near == far) {
            throw new IllegalArgumentException("near == far");
        }

        final float r_width = 1f/(right - left);
        final float r_height = 1f/(top - bottom);
        final float r_depth = 1f/(near - far);

        this.arr[0] = 2f * r_width;     // X
        this.arr[1] = 0f;
        this.arr[2] = 0f;
        this.arr[3] = 0f;

        this.arr[4] = 0f;
        this.arr[5] = 2f * r_height;    // Y
        this.arr[6] = 0f;
        this.arr[7] = 0f;

        this.arr[8]  = 0f;              // A
        this.arr[9]  = 0f;              // B
        this.arr[10] = -2f * r_depth;   // C
        this.arr[11] = 0f;

        this.arr[12] = -(right + left) * r_width;
        this.arr[13] = -(top + bottom) * r_height;
        this.arr[14] = -(far + near)   * r_depth;
        this.arr[15] = 1f;
        return this;
    }

    // Создание матрицы взгляда. ВНИМАНИЕ! dir - вектор направления взгляда, а не точка
    public Mat4f setLookAt(Vec3f eye, Vec3f dir, Vec3f up){
        return setLookAt(eye, dir, up, new Vec3f());
    }

    public Mat4f setLookAt(Vec3f eye, Vec3f dir, Vec3f up, Vec3f right){
        // Нормализуем вектор направления взгляда
        dir.normalize();

        // Создаем единичный вектор направления направо Right = Direction x Up (векторное произведение)
        right.vecProdIn(dir, up);
        right.normalize();

        // Пересчитываем направление наверх относительно новых направлений на цель и направо
        up.vecProdIn(right, dir);

        this.arr[0] = right.x; this.arr[4] = right.y; this.arr[8]  = right.z; this.arr[12] = 0;
        this.arr[1] =    up.x; this.arr[5] =    up.y; this.arr[9]  =    up.z; this.arr[13] = 0;
        this.arr[2] =  -dir.x; this.arr[6] =  -dir.y; this.arr[10] =  -dir.z; this.arr[14] = 0;
        this.arr[3] =       0; this.arr[7] =       0; this.arr[11] =       0; this.arr[15] = 1;

        // Добавляем координаты расположения камеры
        translate(-eye.x, -eye.y, -eye.z);
        return this;
    }

    // Создание матрицы перспективной проекции, заданной плоскостями
    public Mat4f setFrustum(float left, float right,
                            float bottom, float top,
                            float near, float far){
        if (left == right) {
            throw new IllegalArgumentException("left == right");
        }
        if (top == bottom) {
            throw new IllegalArgumentException("top == bottom");
        }
        if (near == far) {
            throw new IllegalArgumentException("near == far");
        }
        if (near <= 0f) {
            throw new IllegalArgumentException("near <= 0f");
        }
        if (far <= 0f) {
            throw new IllegalArgumentException("far <= 0f");
        }

        final float r_width = 1f/(right - left);
        final float r_height = 1f/(top - bottom);
        final float r_depth = 1f/(near - far);

        this.arr[0]  = 2f * near * r_width;       // X
        this.arr[1] = 0f;
        this.arr[2] = 0f;
        this.arr[3] = 0f;

        this.arr[4] = 0f;
        this.arr[5] = 2f * near * r_height;      // Y
        this.arr[6] = 0f;
        this.arr[7] = 0f;

        this.arr[8]  = (right + left)/r_width;    // A
        this.arr[9]  = (top + bottom)/r_height;   // B
        this.arr[10] = (far + near)/r_depth;      // C
        this.arr[11] = -1f;

        this.arr[12] = 0f;
        this.arr[13] = 0f;
        this.arr[14] = 2f * far * near * r_depth; // D
        this.arr[15] = 0f;
        return this;
    }

    // Создание матрицы перспективной проекции, заданной отношениями и углом обзора
    public Mat4f setPerspective(float yFovInDegrees, float aspect, float near, float far) {
        final float a = (float) (1.0f / Math.tan(yFovInDegrees * toRadFactor/2));
        final float depth = 1f/(near - far);

        this.arr[0] = a / aspect;
        this.arr[1] = 0;
        this.arr[2] = 0;
        this.arr[3] = 0;

        this.arr[4] = 0f;
        this.arr[5] = a;
        this.arr[6] = 0f;
        this.arr[7] = 0f;

        this.arr[8] = 0f;
        this.arr[9] = 0;
        this.arr[10] = (far + near) * depth;
        this.arr[11] = -1f;

        this.arr[12] = 0f;
        this.arr[13] = 0f;
        this.arr[14] = 2f * far * near * depth;
        this.arr[15] = 0f;
        return this;
    }

    // Обращение матрицы в единичную
    public Mat4f setIden() {
        int offset = 1;
        for (int i = 0; i < 3; i++, offset++) {
            for (int j = 0; j < 4; j++)
                this.arr[offset++] = 0f;
        }
        for (int i = 0; i < 16; i+=5) {
            this.arr[i] = 1f;
        }
        return this;
    }

    // Создание единичной матрицы
    public static Mat4f genIden() {
        return new Mat4f().setIden();
    }
}
