package nktl.GL4;


import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import nktl.GL4.util.Mat3f;
import nktl.GL4.util.Mat4f;
import nktl.GL4.util.Vec3f;
import nktl.GL4.util.Vec4f;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

import static com.jogamp.opengl.GL.GL_FALSE;
import static com.jogamp.opengl.GL.GL_FLOAT;
import static com.jogamp.opengl.GL.GL_RENDERER;
import static com.jogamp.opengl.GL.GL_UNSIGNED_INT;
import static com.jogamp.opengl.GL.GL_VENDOR;
import static com.jogamp.opengl.GL.GL_VERSION;
import static com.jogamp.opengl.GL2ES2.GL_ACTIVE_ATTRIBUTES;
import static com.jogamp.opengl.GL2ES2.GL_ACTIVE_ATTRIBUTE_MAX_LENGTH;
import static com.jogamp.opengl.GL2ES2.GL_ACTIVE_UNIFORMS;
import static com.jogamp.opengl.GL2ES2.GL_ACTIVE_UNIFORM_MAX_LENGTH;
import static com.jogamp.opengl.GL2ES2.GL_ATTACHED_SHADERS;
import static com.jogamp.opengl.GL2ES2.GL_BOOL;
import static com.jogamp.opengl.GL2ES2.GL_COMPILE_STATUS;
import static com.jogamp.opengl.GL2ES2.GL_FLOAT_MAT2;
import static com.jogamp.opengl.GL2ES2.GL_FLOAT_MAT3;
import static com.jogamp.opengl.GL2ES2.GL_FLOAT_MAT4;
import static com.jogamp.opengl.GL2ES2.GL_FLOAT_VEC2;
import static com.jogamp.opengl.GL2ES2.GL_FLOAT_VEC3;
import static com.jogamp.opengl.GL2ES2.GL_FLOAT_VEC4;
import static com.jogamp.opengl.GL2ES2.GL_FRAGMENT_SHADER;
import static com.jogamp.opengl.GL2ES2.GL_INFO_LOG_LENGTH;
import static com.jogamp.opengl.GL2ES2.GL_INT;
import static com.jogamp.opengl.GL2ES2.GL_LINK_STATUS;
import static com.jogamp.opengl.GL2ES2.GL_SHADING_LANGUAGE_VERSION;
import static com.jogamp.opengl.GL2ES2.GL_VALIDATE_STATUS;
import static com.jogamp.opengl.GL2ES2.GL_VERTEX_SHADER;
import static com.jogamp.opengl.GL2ES3.GL_ACTIVE_UNIFORM_BLOCKS;
import static com.jogamp.opengl.GL2ES3.GL_ACTIVE_UNIFORM_BLOCK_MAX_NAME_LENGTH;
import static com.jogamp.opengl.GL2ES3.GL_MAJOR_VERSION;
import static com.jogamp.opengl.GL2ES3.GL_MINOR_VERSION;
import static com.jogamp.opengl.GL2ES3.GL_UNIFORM_BLOCK_ACTIVE_UNIFORMS;
import static com.jogamp.opengl.GL2ES3.GL_UNIFORM_BLOCK_ACTIVE_UNIFORM_INDICES;
import static com.jogamp.opengl.GL2ES3.GL_UNIFORM_BLOCK_BINDING;
import static com.jogamp.opengl.GL2GL3.GL_DOUBLE;
import static com.jogamp.opengl.GL3.*;

/**
 * Для справки: константами в коментариях названы uniform-переменные
 *
 * Created by Zheka Grushevskiy, NAKATEEL, 15.08.2016.
 */
public class ZShader {

    private final static int[] temp = new int[1];

    /*
     1) Сначала разные константы, перечисления и прочие статические неизменяемые вещи.
     */
    private static final String
            ERR_PROGRAM_VALIDATE = "Программа не действительна.",
            ERR_PROGRAM_NOT_READY = "Программа не готова к использованию.",
            ERR_PROGRAM_NOT_FOUND = "Программа не была создана.",
            ERR_SHADER_TYPE_UNKNOWN = "Неизвестный тип шейдера. ",
            ERR_FILE_NOT_FOUND = "Файл не найден. ",
            ERR_READ_SHADER_FILE = "Ошибка чтения файла шейдера. ",
            ERR_SHADER_CREATE = "Ошибка создания шейдера. ",
            ERR_SHADER_COMPILE = " Ошибка компиляции шейдера. ",
            ERR_PROGRAM_CREATE = "Ошибка создания программы.",
            ERR_PROGRAM_LINK = "Ошибка соединения программы.";

    /**
     * Класс-перечисление кодов шейдеров. Чтобы не париться лишний раз. Плюс это много
     * нормальных таких плюх дает для удобства использования (сразу есть строка-тип шейдера
     * можно код шейдера вытаскивать норм).
     */
    public enum ZShaderType {
        VERTEX(GL_VERTEX_SHADER),
        FRAGMENT(GL_FRAGMENT_SHADER),
        GEOMETRY(GL_GEOMETRY_SHADER),
        TESS_CONTROL(GL_TESS_CONTROL_SHADER),
        TESS_EVALUATION(GL_TESS_EVALUATION_SHADER),
        COMPUTE(GL_COMPUTE_SHADER);

        private static HashMap<String, ZShaderType> extensions = new HashMap<>();
        static {
            extensions.put(".vs", VERTEX);
            extensions.put(".vert", VERTEX);
            extensions.put(".gs", GEOMETRY);
            extensions.put(".geom", GEOMETRY);
            extensions.put(".tcs", TESS_CONTROL);
            extensions.put(".tes", TESS_EVALUATION);
            extensions.put(".fs", FRAGMENT);
            extensions.put(".frag", FRAGMENT);
            extensions.put(".cs", COMPUTE);
        }

        public static ZShaderType getType(String extension){
            if (extensions.containsKey(extension))
                return extensions.get(extension);
            else return null;
        }

        final int code;
        ZShaderType(int code){
            this.code = code;
        }
    }

    /*
     2) Далее описание класса ZShader
     */
    // Сначала переменные:
    private int handle = 0;
    private boolean linked = false;
    private HashMap<String, Integer> uniformLocations = new HashMap<>();
    private GL4 gl = null;

    // Конструкторы
    public ZShader(){}
    public ZShader(GL4 gl){ this.gl = gl; }

    // PRIVATE-методы
    private int getUniformLocation(GL4 gl, String uniformName){
        if (!uniformLocations.containsKey(uniformName))
            uniformLocations.put(uniformName, gl.glGetUniformLocation(this.handle, uniformName));
        return uniformLocations.get(uniformName);
    }

    private void getSubroutineLocation(GL4 gl, String subroutineName, int shader_type, int[] dst){
        dst[0] = gl.glGetSubroutineIndex(handle, shader_type, subroutineName);
    }

    // Чтение файла шейдера
    private String readShaderFile(File file){
        StringBuilder body = new StringBuilder();
        BufferedReader reader = null;
        String code = null;

        try {
            reader = new BufferedReader(new FileReader(file));
            String str;
            while ((str = reader.readLine()) != null) {
                body.append(str);
                body.append("\n");
            }
            code = body.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return code;
    }
    // Сюда потом еще напихать можно чего-то например

    // PUBLIC-методы
    /*
     ПРИСОЕДИНЯТОРЫ ШЕЙДЕРОВ
     */

    // Присоединение шейдера с автоматическим определением типа по расширению
    public void addShader(String folder, String file) throws ZShaderException{
        addShader(gl, folder, file);
    }
    public void addShader(GL4 gl, String folder, String file) throws ZShaderException {
        // Находим индекс символа, с которого начинается расширение файла
        int indOfExtension = file.lastIndexOf(".");
        // Если индекс лежит в пределах строки, то ищем тип при помощи метода в инумераторе. Иначе - зануляем тип.
        ZShaderType type = indOfExtension < 0 ? null : ZShaderType.getType(file.substring(indOfExtension));

        if (type == null) // Если тип равен нулю, выкидываем исключение онеизвестном типе шейдера
            throw new ZShaderException(ERR_SHADER_TYPE_UNKNOWN + file);
        // А если все норм, то просто аттачим шейдер
        addShader(gl, folder, file, type);
    }

    // Присоединение шейдера с явным указанием типа
    public void addShader(String folder, String fileName, ZShaderType type) throws ZShaderException {
        addShader(gl, folder, fileName, type);
    }
    public void addShader(GL4 gl, String folder, String fileName, ZShaderType type) throws ZShaderException {
        // Смотрим что за файл у нас тут
        File file = new File(folder, fileName);
        if (!file.exists()) // если он не существует, то выкидываем исключение
            throw new ZShaderException(ERR_FILE_NOT_FOUND + file.getAbsolutePath());
        String source = readShaderFile(file); // Читаем сурс
        if (source == null) // Если не получилось, то выкидываем исключение
            throw new ZShaderException(ERR_READ_SHADER_FILE + type);
        addShader(gl, source, type); // А если все норм, то пробуем аттачить шейдер дальше
    }

    // Присоединение шейдера из исходного кода с явным указанием типа
    public void addShader(String source, ZShaderType type) throws ZShaderException {
        addShader(gl, source, type);
    }
    public void addShader(GL4 gl, String source, ZShaderType type) throws ZShaderException {
        if (this.handle <= 0){ // Если у нас нет еще шейдеропрогаммы, то пробуем её создать
            this.handle = gl.glCreateProgram();
            if (this.handle == 0) // И если у нас нифига не получается, то выкидываем исключение
                throw new ZShaderException(ERR_PROGRAM_CREATE);
        }
        // Если все норм, создаем и аттачим шейдер. Все возможные ошибки уже учтены в compileShader()
        gl.glAttachShader(this.handle, compileShader(gl, source, type));
    }

    /*
     СОЕДИНЯТОРЫ, ЮЗАТОРЫ И ПРОВЕРЯТОРЫ ПРОГРАММЫ
     */

    // Соединятор программы
    public void link() throws ZShaderException { link(gl); }
    public void link(GL4 gl) throws ZShaderException {
        if (this.linked) return;
        if (this.handle <= 0)
            throw new ZShaderException(ERR_PROGRAM_NOT_FOUND);
        gl.glLinkProgram(this.handle);
        int[]status = new int[1];
        gl.glGetProgramiv(this.handle, GL_LINK_STATUS, status, 0);
        if (status[0] == GL_FALSE)
            throw new ZShaderException(ERR_PROGRAM_LINK + '\n'
                    + getProgramInfoLog(gl, this.handle));
        else {
            uniformLocations.clear();
            linked = true;
        }
    }

    // Попытка использования программы
    public void use() throws ZShaderException { use(gl); }
    public void use(GL4 gl) throws ZShaderException {
        if ( this.handle <= 0 || !this.linked )
            throw new ZShaderException(ERR_PROGRAM_NOT_READY);
        gl.glUseProgram(this.handle);
    }

    // Проверка действительности программы
    public void validate() throws ZShaderException { validate(gl); }
    public void validate(GL4 gl) throws ZShaderException {
        if (this.handle <= 0 || !this.linked)
            throw new ZShaderException(ERR_PROGRAM_NOT_READY);

        int[]status = new int[1];
        gl.glValidateProgram(this.handle);
        gl.glGetProgramiv(this.handle, GL_VALIDATE_STATUS, status, 0);
        if (status[0] == GL_FALSE)
            throw new ZShaderException(ERR_PROGRAM_VALIDATE + '\n'
                    + getProgramInfoLog(gl, this.handle));
    }

    // Получение указателя на программу
    public int getHandle(){ return this.handle; }
    // Получения статуса соединенности программы
    public boolean isLinked(){ return this.linked; }

    /*
     БИНДИТЕЛИ АТРИБУТОВ И УСТАНОВЩИКИ КОНСТАНТ
     */

    // Для атрибутов
    public void bindAttributeLocation(int location, String name) {
        bindAttributeLocation(gl, location, name);
    }
    public void bindAttributeLocation(GL4 gl, int location, String name){
        gl.glBindAttribLocation(this.handle, location, name);
    }

    // Для фрагментов
    public void bindFragDataLocation(int location, String name){
        bindFragDataLocation(gl, location, name);
    }
    public void bindFragDataLocation(GL4 gl, int location, String name){
        gl.glBindFragDataLocation(this.handle, location, name);
    }

    // Для констант
    // НОВЫЕ
    public void setUniform(String name, Vec3f vec) {
        setUniform(gl, name, vec);
    }
    public void setUniform(GL4 gl, String name, Vec3f vec){
        setUniform(gl, name, vec.x, vec.y, vec.z);
    }

    public void setUniform(String name, Vec4f vec) {
        setUniform(gl, name, vec);
    }
    public void setUniform(GL4 gl, String name, Vec4f vec){
        setUniform(gl, name, vec.x, vec.y, vec.z, vec.w);
    }

    public void setUniform(String name, Mat3f mat){
        setUniform(gl, name, mat);
    }
    public void setUniform(GL4 gl, String name, Mat3f mat){
        gl.glUniformMatrix3fv(getUniformLocation(gl, name), 1, false, mat.arr(), 0);
    }

    public void setUniform(String name, Mat4f mat){
        setUniform(gl, name, mat);
    }
    public void setUniform(GL4 gl, String name, Mat4f mat){
        gl.glUniformMatrix4fv(getUniformLocation(gl, name), 1, false, mat.arr(), 0);
    }


    public void setUniform(String name, float value){
        setUniform(gl, name, value);
    }
    public void setUniform(GL4 gl, String name, float value){
        gl.glUniform1f(getUniformLocation(gl, name), value);
    }

    public void setUniform(String name, int value){
        setUniform(gl, name, value);
    }
    public void setUniform(GL4 gl, String name, int value){
        gl.glUniform1i(getUniformLocation(gl, name), value);
    }

    public void setUniform(String name, boolean value){
        setUniform(gl, name, value);
    }
    public void setUniform(GL4 gl, String name, boolean value){
        setUniform(gl, name, value ? 1 : 0);
    }

    // еще для векторов
    public void setUniform(String name, float x, float y, float z){
        setUniform(gl, name, x, y, z);
    }
    public void setUniform(GL4 gl, String name, float x, float y, float z){
        gl.glUniform3f(getUniformLocation(gl, name), x, y, z);
    }

    public void setUniform(String name, float x, float y, float z, float w){
        setUniform(gl, name, x, y, z, w);
    }
    public void setUniform(GL4 gl, String name, float x, float y, float z, float w){
        gl.glUniform4f(getUniformLocation(gl, name), x, y, z, w);
    }

    // Установщик объекта OpenGL4 (чтобы постоянно не надо было его передавать например)
    public void setGLObj(GL4 gl){
        this.gl = gl;
    }

    private void pickSubroutine(String name, int shader_type){
        synchronized (temp) {
            getSubroutineLocation(gl, name, shader_type, temp);
            gl.glUniformSubroutinesuiv(shader_type, 1, temp, 0);
        }
    }

    public void pickSubroutineVS(String name){
        pickSubroutine(name, GL_VERTEX_SHADER);
    }

    public void pickSubroutineFS(String name){
        pickSubroutine(name, GL_FRAGMENT_SHADER);
    }

    // СТАРЫЕ
    // 1. Трехмерные вектора
    @Deprecated public void setUniform3vf(GL4 gl, String name, float x, float y, float z){
        gl.glUniform3f(getUniformLocation(gl, name), x, y, z);
    }

    // 2. Четырехмерные вектора
    @Deprecated public void setUniform4vf(GL4 gl, String name, float x, float y, float z, float w){
        gl.glUniform4f(getUniformLocation(gl, name), x, y, z, w);
    }

    @Deprecated public void setUniform4vf(GL4 gl, String name, float[] vec){
        this.setUniform4vf(gl, name, vec[0], vec[1], vec[2], vec[3]);
    }

    // 3. Для матриц
    @Deprecated public void setUniform3mf(GL4 gl, String name, float[] mat3f){
        gl.glUniformMatrix3fv(getUniformLocation(gl, name), 1, false, mat3f, 0);
    }

    @Deprecated public void setUniform4mf(GL4 gl, String name, float[] mat4f){
        gl.glUniformMatrix4fv(getUniformLocation(gl, name), 1, false, mat4f, 0);
    }

    @Deprecated public void setUniform1f(GL4 gl, String name, float value){
        gl.glUniform1f(getUniformLocation(gl, name), value);
    }

    /*
     Выводители информации об атрибутах, константах и блоках констант
     */

    // Печатает активные константы
    public void printActiveUniforms() { printActiveUniforms(gl);}
    public void printActiveUniforms(GL4 gl){
        int[] version = getOpenGLVersion(gl);
        System.out.println("Активные константы:");
        if (version[1] < 3){
            // nslm = NumOfUniforms (0), Size (1), Location (2), MaxLength (3)
            int[] nslm = new int[4];
            gl.glGetProgramiv( this.handle, GL_ACTIVE_UNIFORM_MAX_LENGTH, nslm, 3);
            gl.glGetProgramiv( this.handle, GL_ACTIVE_UNIFORMS, nslm, 0);

            byte[] nameBytes = new byte[nslm[3]];
            int[] type = new int[1];

            for (int i = 0; i < nslm[0]; i++){
                gl.glGetActiveUniform( this.handle, i, nslm[3], new int[1], 0, nslm, 1, type, 0, nameBytes, 0);
                String name = new String(nameBytes).trim();
                nslm[2] = gl.glGetUniformLocation(this.handle, name);
                System.out.println(' ' + nslm[2] + ' ' + name + " (" + getTypeString(type[0]) + ')');
            }
        } else {
            int[] properties = { GL_NAME_LENGTH, GL_TYPE, GL_LOCATION, GL_BLOCK_INDEX };
            int[] numOfUniforms = new int[1];
            gl.glGetProgramInterfaceiv(this.handle, GL_UNIFORM, GL_ACTIVE_RESOURCES, numOfUniforms, 0);
            int[] results = new int[4];
            for (int i = 0; i < numOfUniforms[0]; i++){
                gl.glGetProgramResourceiv(this.handle, GL_UNIFORM, i, 4, properties, 0, 4, null, 0, results, 0);
                if (results[3] != -1) continue;
                int nameBufferSize = results[0];
                ByteBuffer nameBuffer = Buffers.newDirectByteBuffer(nameBufferSize);
                gl.glGetProgramResourceName(this.handle, GL_UNIFORM, i, nameBufferSize, null, nameBuffer);
                System.out.println(' ' + results[2] + ' ' + byteBufferToString(nameBuffer).trim() + " (" + getTypeString(results[1]) + ')');
            }
        }
    }

    // Печатает активные блоки констант (uniform-блоки)
    public void printActiveUniformBlocks() {printActiveUniformBlocks(gl);}
    public void printActiveUniformBlocks(GL4 gl){
        int[] version = getOpenGLVersion(gl);
        System.out.println("Активные блоки констант:");

        if (version[1] < 3){
            // Параметры: written(0), maxLength(1), maxUniLen(2), nBlocks(3), binding(4)
            int[] params = new int[5];

            gl.glGetProgramiv(this.handle, GL_ACTIVE_UNIFORM_BLOCK_MAX_NAME_LENGTH, params, 1);
            gl.glGetProgramiv(this.handle, GL_ACTIVE_UNIFORM_BLOCKS, params, 3);
            gl.glGetProgramiv(this.handle, GL_ACTIVE_UNIFORM_MAX_LENGTH, params, 2);

            byte[] uniName = new byte[params[2]];
            byte[] nameBytes = new byte[params[1]];

            for( int i = 0; i < params[3]; i++){
                gl.glGetActiveUniformBlockName(this.handle, i, params[1], params, 0, nameBytes, 0);
                gl.glGetActiveUniformBlockiv(this.handle, i, GL_UNIFORM_BLOCK_BINDING, params, 4);
                System.out.println(" Uniform block \"" + new String(nameBytes).trim() + "\" (" + params[4] + "):");

                int[] nUnits = new int[1];
                gl.glGetActiveUniformBlockiv(this.handle, i, GL_UNIFORM_BLOCK_ACTIVE_UNIFORMS, nUnits, 0);
                int[] uIndexes = new int[nUnits[0]];
                gl.glGetActiveUniformBlockiv(this.handle, i, GL_UNIFORM_BLOCK_ACTIVE_UNIFORM_INDICES, uIndexes, 0);

                for (int uIndex : uIndexes) {
                    int[] sizeAndType = new int[2];

                    gl.glGetActiveUniform(this.handle, uIndex, params[2],
                            params, 0, sizeAndType, 0, sizeAndType, 1, uniName, 0);
                    System.out.println("\t " + new String(uniName).trim() + " (" + getTypeString(sizeAndType[1]) + ')');
                }
            }
        } else {
            int[] numOfBlocks = new int[1];
            gl.glGetProgramInterfaceiv(this.handle, GL_UNIFORM_BLOCK, GL_ACTIVE_RESOURCES, numOfBlocks, 0);
            int[]
                    blockProps = { GL_NUM_ACTIVE_VARIABLES, GL_NAME_LENGTH },
                    blockIndex = { GL_ACTIVE_VARIABLES },
                    props = { GL_NAME_LENGTH, GL_TYPE, GL_BLOCK_INDEX };

            for (int block = 0; block < numOfBlocks[0]; block++){
                int[] blockInfo = new int[2];
                gl.glGetProgramResourceiv(this.handle, GL_UNIFORM_BLOCK, block, 2, blockProps, 0, 2, null, 0, blockInfo, 0);

                byte[] blockName = new byte[blockInfo[1]];
                gl.glGetProgramResourceName(this.handle, GL_UNIFORM_BLOCK, block, blockInfo[1], null, 0, blockName, 0);
                System.out.println(" Uniform block \"" + new String(blockName).trim() + "\":");

                int[] uIndexes = new int[blockInfo[0]];
                gl.glGetProgramResourceiv(this.handle, GL_UNIFORM_BLOCK, block, 1,
                        blockIndex, 0, uIndexes.length, null, 0, uIndexes, 0);

                for (int uIndex : uIndexes) {
                    int[] results = new int[3];
                    gl.glGetProgramResourceiv(this.handle, GL_UNIFORM, uIndex, 3, props, 0, 3, null, 0, results, 0);

                    byte[] nameBytes = new byte[results[0]];
                    gl.glGetProgramResourceName(this.handle, GL_UNIFORM, uIndex, results[0], null, 0, nameBytes, 0);
                    System.out.println("\t " + new String(nameBytes).trim() + " (" + getTypeString(results[1]) + ')');
                }
            }
        }
    }

    // Печатает активные атрибуты
    public void printActiveAttributes(){ printActiveAttributes(gl); }
    public void printActiveAttributes(GL4 gl){

        System.out.println("Активные атрибуты:");
        if (getOpenGLVersion(gl)[1] < 3){
            // Параметры: written(0), size(1), location(2), maxLength(3), nAttribs(4), type(5)
            int[] params = new int[6];

            gl.glGetProgramiv(this.handle, GL_ACTIVE_ATTRIBUTE_MAX_LENGTH, params, 3);
            gl.glGetProgramiv(this.handle, GL_ACTIVE_ATTRIBUTES, params, 4);

            byte[] nameBytes = new byte[params[3]];
            for (int i = 0; i < params[4]; i++){
                gl.glGetActiveAttrib(this.handle, i, params[3], params, 0, params, 1, params, 5, nameBytes, 0);
                String name = new String(nameBytes).trim();
                params[2] = gl.glGetAttribLocation(this.handle, name);
                System.out.println(' ' + params[2] + ' ' + name + " (" + getTypeString(params[5]) + ')');
            }
        } else {
            int[] numAttribs = new int[1];
            gl.glGetProgramInterfaceiv( this.handle, GL_PROGRAM_INPUT, GL_ACTIVE_RESOURCES, numAttribs, 0);
            int[] properties = { GL_NAME_LENGTH, GL_TYPE, GL_LOCATION };

            for (int i = 0; i < numAttribs[0]; i++){
                int[] results = new int[3];
                gl.glGetProgramResourceiv(this.handle, GL_PROGRAM_INPUT,
                        i, 3, properties, 0, 3, null, 0, results, 0);
                byte[] nameBytes = new byte[results[0]];
                gl.glGetProgramResourceName(this.handle, GL_PROGRAM_INPUT, i, results[0], null, 0, nameBytes, 0);
                System.out.println(' ' + results[2] + ' ' + new String(nameBytes).trim() + " (" + getTypeString(results[1]) + ')');
            }
        }
    }

    /*
     Высвободитель программы
     */
    public void dispose() { dispose(gl); }
    public void dispose(GL4 gl){
        if (this.handle == 0) return;

        // Сначала достаем кол-во пределанных шейдеров
        int[] numOfShaders = new int[1];
        gl.glGetProgramiv(this.handle, GL_ATTACHED_SHADERS, numOfShaders, 0);

        // Достаем указатели на шейдеры
        if (numOfShaders[0] > 0){
            int[] shaderHandles = new int[numOfShaders[0]];
            gl.glGetAttachedShaders(this.handle, numOfShaders[0], new int[1], 0, shaderHandles, 0);

            // Удаляем шейдеры
            for (int shaderHandle : shaderHandles)
                gl.glDeleteShader(shaderHandle);
        }

        // Удаляем программу
        gl.glDeleteProgram(this.handle);

        // Все (^ - ^)
    }

    /*
     3) Далее статические методы и классы
     */

    /**
     * Класс для генерации исключений при компиляции и объединении шейдеров
     */
    public static class ZShaderException extends Exception {
        ZShaderException(String message){
            super(message);
        }
    }

    // Компиляция шейдера
    private static int compileShader(GL4 gl, String source, ZShaderType type) throws ZShaderException {
        int shaderHandle = gl.glCreateShader(type.code);
        if (shaderHandle == 0)
            throw new ZShaderException(ERR_SHADER_CREATE);
        // Передаем сурс
        gl.glShaderSource(shaderHandle, 1, new String[]{source}, null, 0);
        // Компилируем
        gl.glCompileShader(shaderHandle);
        int status[] = new int[1];
        gl.glGetShaderiv(shaderHandle, GL_COMPILE_STATUS, status, 0);
        if (status[0] == GL_FALSE){
            String msg = ERR_SHADER_COMPILE + type +
                    getShaderInfoLog(gl, shaderHandle);
            throw new ZShaderException(msg);
        } else {
            return shaderHandle;
        }
    }

    // Конвертер ByteBuffer в String
    private static String byteBufferToString(ByteBuffer bb){
        if (bb.limit() > 0){
            byte[] bytes = new byte[bb.limit()];
            bb.get(bytes);
            return new String(bytes);
        } else return "";
    }

    // Получение лога программы или шейдера
    private static String getInfoLog(GL4 gl, int handle, int type){
        int[]length = new int[1];
        switch(type){
            case 0:
                gl.glGetShaderiv(handle, GL_INFO_LOG_LENGTH, length, 0);
                break;
            case 1:
                gl.glGetProgramiv(handle, GL_INFO_LOG_LENGTH, length, 0);
                break;
            default: return "";
        }
        if (length[0] > 0){
            byte[] bytes = new byte[length[0]];
            switch(type){
                case 0:
                    gl.glGetShaderInfoLog(handle, length[0], new int[1], 0, bytes, 0);
                    break;
                case 1:
                    gl.glGetProgramInfoLog(handle, length[0], new int[1], 0, bytes, 0);
                    break;
                default: return "";
            }
            return new String(bytes);
        } else return "";
    }

    // Получение лога программы
    private static String getProgramInfoLog(GL4 gl, int programHandle){
        return getInfoLog(gl, programHandle, 1);
    }

    // Получение лога шейдера
    private static String getShaderInfoLog(GL4 gl, int shaderHandle){
        return getInfoLog(gl, shaderHandle, 0);
    }

    private static String getTypeString(int type){
        switch(type){
            case GL_FLOAT:
                return "float";
            case GL_FLOAT_VEC2:
                return "vec2";
            case GL_FLOAT_VEC3:
                return "vec3";
            case GL_FLOAT_VEC4:
                return "vec4";
            case GL_DOUBLE:
                return "double";
            case GL_INT:
                return "int";
            case GL_UNSIGNED_INT:
                return "unsigned int";
            case GL_BOOL:
                return "bool";
            case GL_FLOAT_MAT2:
                return "mat2";
            case GL_FLOAT_MAT3:
                return "mat3";
            case GL_FLOAT_MAT4:
                return "mat4";
            default:
                return "?";
        }
    }

    private static int[] getOpenGLVersion(GL4 gl){
        int[] arr = new int[2];
        gl.glGetIntegerv(GL_MAJOR_VERSION, arr, 0);
        gl.glGetIntegerv(GL_MINOR_VERSION, arr, 1);
        return arr;
    }

    public static void printGLInfo(GL4 gl){
        int[] arr = getOpenGLVersion(gl);
        String sb = "OpenGL version " + gl.glGetString(GL_VERSION) +
                "; major: " + arr[0] + "; minor: " + arr[1] +
                '\n' + gl.glGetString(GL_SHADING_LANGUAGE_VERSION) +
                '\n' + gl.glGetString(GL_RENDERER) +
                '\n' + gl.glGetString(GL_VENDOR);

        System.out.println(sb);
    }

    public static ZShader compileAndLinkProgram(GL4 gl, String folder, String... filenames){
        ZShader shader = null;
        try {
            shader = new ZShader(gl);
            for (String filename : filenames){
                shader.addShader(folder, filename);
            }
            shader.link();
        } catch (ZShaderException e) {
            e.printStackTrace();
            shader.dispose();
            return null;
        }
        return shader;
    }
}
