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

import java.util.Collections;
import java.util.LinkedList;

import static com.jogamp.opengl.GL4.*;

/**
 * Created by Zheka Grushevskiy, NAKATEEL, 30.11.2016.
 */
public class Render extends ZRender {
    private Vec4f lightPosition_world = new Vec4f(2f, 2f, 0f, 1f);
    private Vec4f lightPosition_cam = new Vec4f();
    private int[] vao = new int[1];
    private int indNum = 0;
    private int triNum = 0;

    @Override
    public void init(GLAutoDrawable glad) {
        super.init(glad);
        GL4 gl = glad.getGL().getGL4();
        gl.glEnable(GL_DEPTH_TEST);
        //gl.glEnable(GL_BLEND);
        //gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL_CULL_FACE);
        //gl.glCullFace(GL_BACK);

        DwarfGen generator = new DwarfGen();
        generator.getSet().setSeed(45825243);
        DwarfMap map;
        LinkedList<Cube> cubes = new LinkedList<>();

        try {
            map = generator.genMap(new Vec3i(10, 10, 10), null);
            Vec3i main = map.graph().getNodes().next().data().position();//cubes.getFirst().getVertices()[0];
            cam.setPosition(main.x, main.y, main.z);

            for (DwarfCube cube : map.cubes()){
                cubes.add(new Cube(0, Cube.zxy(cube.position())));
            }
        } catch (GeneratorException e) {
            e.printStackTrace();
        }

        LinkedList<Triangle> tsList = new LinkedList<>();
        for (Cube c : cubes) {
            Triangle[] locts = c.getTriangles();
            Collections.addAll(tsList, locts);
        }

        Triangle[]ts = tsList.toArray(new Triangle[0]);
        triNum = ts.length;

        System.out.println("Triangles : " + ts.length);

        float[] kek = new float[ts.length * 9];
        for (int i = 0; i < ts.length; i++) {
            System.out.println("\nTriangle " + i);
            int j = i*9;
            for (Vertex v : ts[i].getVs()){
                kek[j] = v.x;
                kek[j+1] = v.y;
                kek[j+2] = v.z;
                j+=3;
            }
        }


        ZModelAdapter.createVAO(gl, kek, vao, 0, 3);

        //ZModelAdapter.createVAO(gl, points, vao, 0, 3, 4);
        //ZModelAdapter.addIndexesToVAO(gl, indices, vao, 0);

        ZShader prog = new ZShader();

        try {
            prog.setGLObj(gl);
            prog.addShader(gl, "dwarf", "demo.vert");
            prog.addShader(gl, "dwarf", "demo.geom");
            prog.addShader(gl, "dwarf", "demo.frag");
            prog.link();
            prog.validate();
            progs.put("shader", prog);
            prog.use();

            prog.setUniform("color", new Vec4f(0.5f, 0.5f, 0.5f, 1f));
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

            gl.glBindVertexArray(vao[0]);
            gl.glDrawArrays(GL_TRIANGLES, 0, triNum*3);
        } catch (ZShader.ZShaderException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dispose(GLAutoDrawable glad) {
        super.dispose(glad);
        GL4 gl = toGL(glad);
        gl.glDeleteVertexArrays(1, vao, 0);
    }
}
