package nktl.dwarf.graphics;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import nktl.GL4.ZModelAdapter;
import nktl.GL4.ZRender;
import nktl.GL4.ZShader;
import nktl.GL4.util.Mat4f;
import nktl.GL4.util.Vec3f;
import nktl.GL4.util.Vec4f;
import nktl.dwarf.DwarfCube;
import nktl.dwarf.DwarfGen;
import nktl.dwarf.DwarfMap;
import nktl.dwarf.GeneratorException;
import nktl.math.geom.Vec3i;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import static com.jogamp.opengl.GL4.*;

/**
 * Created by Zheka Grushevskiy, NAKATEEL, 30.11.2016.
 */
public class Render extends ZRender {
    private Vec4f lightPosition_world = new Vec4f(2f, 2f, 0f, 1f);
    private Vec4f lightPosition_cam = new Vec4f();
    private int[] vao;
    private int indNum = 0;
    private Vec4f[] colors;
    private int triNum[];
    private Mat4f vpm = Mat4f.genIden();

    @Override
    public void init(GLAutoDrawable glad) {
        super.init(glad);
        GL4 gl = glad.getGL().getGL4();
        gl.glEnable(GL_DEPTH_TEST);
        //gl.glEnable(GL_BLEND);
        //gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        //gl.glEnable(GL_CULL_FACE);
        //gl.glCullFace(GL_BACK);



        try {
            // Начало картежной фигни
            DwarfGen gen = new DwarfGen();
            gen.settings()
                    .setSeed(45825243)
                    .setLengths(2, 5)
                    .setWayRatio(100, 75, 50, 25, 5, 1)
                    .setDimensions(11, 11, 11);

            DwarfMap map = gen.genMap();
            Vec3i main = map.graph().getNodes().next().data().position();

            LinkedList<DwarfCube> mcubes = map.getCubes();
            System.out.println("Получено кубов: " + mcubes.size());
            var cubeMap = DwarfCube.separate(mcubes);
            // Конец картежной фигни


            cam.setPosition(main.x, main.y, main.z);

            vao = new int[cubeMap.keySet().size()];
            triNum = new int[vao.length];
            colors = new Vec4f[vao.length];

            int offset = 0;
            for (DwarfCube.CubeType type : cubeMap.keySet()){
                asVAO(gl, vao, triNum, offset, cubeMap.get(type));
                colors[offset] = getColor(type);
                offset++;
            }

        } catch (GeneratorException e) {
            e.printStackTrace();
        }

        //ZModelAdapter.createVAO(gl, points, vao, 0, 3, 4);
        //ZModelAdapter.addIndexesToVAO(gl, indices, vao, 0);

        ZShader prog = new ZShader();

        try {
            prog.setGLObj(gl);
            prog.addShader(gl, "res/shader", "demo.vert");
            prog.addShader(gl, "res/shader", "demo.geom");
            prog.addShader(gl, "res/shader", "demo.frag");
            prog.link();
            prog.validate();
            progs.put("shader", prog);
            prog.use();


            prog.setUniform("Specular", new Vec4f(0.8f, 1f));
            prog.setUniform("Intensity", new Vec4f(0.7f, 1f));
            prog.setUniform("light_pos", new Vec3f());

        } catch (ZShader.ZShaderException e) {
            e.printStackTrace();
        }

        double angle = 5 * Math.PI/ 180;
        float sin = (float) Math.sin(angle);
        float cos = (float) Math.cos(angle);
        rot.setBasisRotation(cos, 0, -sin, 0, 1, 0, sin, 0, cos);
    }

    Mat4f rot = Mat4f.genIden();

    @Override
    public void display(GLAutoDrawable glad) {
        super.display(glad);
        GL4 gl = toGL(glad);
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        cam.recountLookAtM();
        cam.recountPVM();

        //lightPosition_world.multiMVIn(rot);
        //lightPosition_cam.multiMVIn(cam.lookAtM, lightPosition_world);

        ZShader prog;
        if ((prog = progs.get("shader")) != null) try{
            prog.use();
            prog.setUniform("MVP", cam.PVM);
            prog.setUniform("ModelViewMatrix", cam.lookAtM);
            prog.setUniform("ViewportMatrix", vpm);

            for (int i = 0; i < vao.length; i++) {
                prog.setUniform("color", colors[i]);
                gl.glBindVertexArray(vao[i]);
                gl.glDrawArrays(GL_TRIANGLES, 0, triNum[i]*3);
            }

        } catch (ZShader.ZShaderException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dispose(GLAutoDrawable glad) {
        super.dispose(glad);
        GL4 gl = toGL(glad);
        if (vao != null)
            gl.glDeleteVertexArrays(vao.length, vao, 0);
    }

    @Override
    public void reshape(GLAutoDrawable glad, int x, int y, int width, int height) {
        super.reshape(glad, x, y, width, height);
        recountVPM(width, height);
    }

    private void recountVPM(int w, int h){
        vpm.arr[0] = vpm.arr[3] = w/2;
        vpm.arr[5] = vpm.arr[7] = h/2;
    }

    private static void asVAO(GL4 gl, int[] vao, int[]triNum, int offset, Collection<DwarfCube> cube_src){
        //LinkedList<Cube> cubes = new LinkedList<>();
        var tsList = new LinkedList<Triangle>();

        for (DwarfCube src_cube : cube_src){
            Triangle[] locTs;

            switch (src_cube.type()){

                //case ROOT_CUBE:
                //case UNKNOWN:
                case TUNNEL:
                    locTs = Element.glTunnel(src_cube); break;
                case LADDER:
                    locTs = Element.glLadder(src_cube); break;
                case COLLECTOR:
                    locTs = Element.glTunnel(src_cube); break;
                case STAIRS:
                    locTs = Element.glStairs(src_cube); break;
                case PLUG:
                    continue;
                default:
                    Cube cube = Cube.make(0, src_cube.position(), src_cube);
                    if (src_cube.features().containsKey(DwarfCube.Feature.WAY))
                        cube.addDirectionBits(src_cube.features().get(DwarfCube.Feature.WAY));
                    //cubes.add(cube);
                    locTs = cube.getTriangles();
                    break;
            }
            if (locTs == null)
                System.out.println("Cube is null. Type : " + src_cube.type());
            if (tsList == null)
                System.out.println("SUPER TUPO");

            Collections.addAll(tsList, locTs);
        }

        //for (Cube c : cubes) {
        //    Triangle[] locts = c.getTriangles();
        //    Collections.addAll(tsList, locts);
        //}

        Triangle[]ts = tsList.toArray(new Triangle[0]);
        triNum[offset] = ts.length;

        float[] kek = new float[ts.length * 9];
        for (int i = 0; i < ts.length; i++) {
            int j = i*9;
            for (Vertex v : ts[i].getVs()){
                kek[j] = v.x;
                kek[j+1] = v.y;
                kek[j+2] = v.z;
                j+=3;
            }
        }


        ZModelAdapter.createVAO(gl, kek, vao, offset, 3);
    }

    private static Vec4f getColor(DwarfCube.CubeType type){
        Vec4f color = new Vec4f();
        switch (type){
            case TUNNEL:
                color.copy(.5f, .7f, .5f, 1f); break;
            case COLLECTOR:
                color.copy(.5f, .5f, .7f, 1f); break;
            case PLUG:
                color.copy(.1f, .1f, .1f, .5f); break;
            case LADDER:
                color.copy(.7f, .7f, .5f, 1f); break;
            case STAIRS:
                color.copy(.7f, .5f, .5f, 1f); break;
            default:
                color.copy(.5f, .5f, .5f, 1f); break;
        }
        return color;
    }
}
