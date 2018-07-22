package nktl.dwarf.graphics;

import nktl.GL4.util.Vec3f;
import nktl.dwarf.DwarfCube;
import nktl.math.geom.Vec3i;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import static nktl.dwarf.DwarfCube.Feature.*;
import static nktl.dwarf.DwarfDirection.*;

public abstract class Element {
    private static final float a = 0.5f;
    private static final float b = 0.3f;
    private static final float c = 0.25f;
    private static final float d = 0.2f;
    private static final float e = 0.1f;
    private static final float f = 0.05f;

    private static final int[] dirs = {
            BIT_POS_X, BIT_POS_Z, BIT_NEG_X, BIT_NEG_Z, BIT_POS_Y, BIT_NEG_Y
    };

    public static Triangle[] glStairs(DwarfCube dCube) {
        var triangles = new LinkedList<Triangle>();
        if (dCube.features().containsKey(DESTRUCTION)){

        } else {
            // TODO: 22.07.2018
        }
        return null;
    }

    public static Triangle[] glLadder(DwarfCube dCube) {
        var triangles = new LinkedList<Triangle>();
        Collections.addAll(triangles, glTunnel(dCube));

        Vec3f neg = new Vec3f(-f, -f, -e);
        Vec3f pos = new Vec3f(f, f, e);
        Vec3i shift = dCube.position();
        int dir = dCube.features().getOrDefault(WAY, 0);
        float start = dirHasBit(dir, BIT_NEG_Y) ? -0.4f : -0.2f;
        float stop = dirHasBit(dir, BIT_POS_Y) ? 0.5f : 0.3f;
        for (float i = start; i < stop; i+=0.2) {
            CubeOut cube = new CubeOut(pos, neg, 0);

            for (Vertex v : cube.vs){
                v.y += i;
                v.plusIn(shift);
            }
            Collections.addAll(triangles, cube.getTriangles());
        }
        return triangles.toArray(new Triangle[0]);
    }

    public static Triangle[] glTunnel(DwarfCube dCube) {
        Vec3f neg = new Vec3f(-b, -b, -b);
        Vec3f pos = new Vec3f(b, b, b);
        int way = dCube.features().getOrDefault(WAY, 0);
        LinkedList<AbsCube> cubes = new LinkedList<>();
        cubes.add(new CubeIn(pos, neg, way));
        for (int side : dirs)
            if ((way & side) == side)
                cubes.add(glWay(side));

        var triangles = new LinkedList<Triangle>();
        Vec3i shift = dCube.position();
        for (AbsCube cube : cubes) {
            for (Vertex v : cube.vs)
                v.plusIn(shift);
            Collections.addAll(triangles, cube.getTriangles());
        }
        if (dCube.features().containsKey(WATER)){
            AbsCube water = new CubeOut(new Vec3f(c, c, c), new Vec3f(-c, -c, -c), 0);
            for (Vertex v : water.vs)
                v.plusIn(shift);
            Collections.addAll(triangles, water.getTriangles());
        }
        return triangles.toArray(new Triangle[0]);
    }

    private static AbsCube glWay(int bit){
        switch (bit){
            case BIT_POS_Y:
                return new CubeIn(new Vec3f(b, a, b), new Vec3f(-b, b, -b),
                        BIT_POS_Y | BIT_NEG_Y);
            case BIT_NEG_Y:
                return new CubeIn(new Vec3f(b, -b, b), new Vec3f(-b, -a, -b),
                        BIT_POS_Y | BIT_NEG_Y);
            default:
                CubeIn way = new CubeIn(new Vec3f(b, b, -b), new Vec3f(-b, -b, -a),
                        BIT_POS_Z | BIT_NEG_Z);
                switch (bit){
                    case BIT_POS_X: way.rotate90(); break;
                    case BIT_POS_Z: way.rotate180(); break;
                    case BIT_NEG_X: way.rotate270(); break;
                }
                return way;
        }
    }

    /*
        ПРОСТЫЕ КУБЫ
     */
    public static abstract class AbsCube {
        protected Vertex[] vs;

        public AbsCube(Vec3f posAng, Vec3f negAng){
            vs = new Vertex[]{
                    new Vertex(negAng.x, negAng.y, negAng.z),
                    new Vertex(negAng.x, negAng.y, posAng.z),
                    new Vertex(negAng.x, posAng.y, negAng.z),
                    new Vertex(negAng.x, posAng.y, posAng.z),
                    new Vertex(posAng.x, negAng.y, negAng.z),
                    new Vertex(posAng.x, negAng.y, posAng.z),
                    new Vertex(posAng.x, posAng.y, negAng.z),
                    new Vertex(posAng.x, posAng.y, posAng.z)
            };
        }
        abstract Triangle[] getTriangles();

        public void rotate90(){
            float temp;
            for (Vertex v : vs){
                temp = v.x;
                v.x = -v.z;
                v.z = temp;
            }
        }

        public void rotate180(){
            for (Vertex v : vs){
                v.x = -v.x;
                v.z = -v.z;
            }
        }
        public void rotate270(){
            float temp;
            for (Vertex v : vs){
                temp = v.x;
                v.x = v.z;
                v.z = -temp;
            }
        }

        Triangle makeTriangle(int ind1, int ind2, int ind3){
            return new Triangle(vs[ind1], vs[ind2], vs[ind3]);
        }
    }

    public static class CubeIn extends AbsCube {
        int dir;
        public CubeIn(Vec3f posAng, Vec3f negAng, int dir) {
            super(posAng, negAng);
            this.dir = dir;
        }

        @Override
        Triangle[] getTriangles() {
            ArrayList<Triangle> triangles = new ArrayList<>();
            if (!dirHasBit(dir, BIT_POS_X)){
                triangles.add(makeTriangle(4, 5, 6));
                triangles.add(makeTriangle(6, 5, 7));
            }
            if (!dirHasBit(dir, BIT_NEG_X)){
                triangles.add(makeTriangle(0, 2, 1));
                triangles.add(makeTriangle(1, 2, 3));
            }
            if (!dirHasBit(dir, BIT_POS_Y)){
                triangles.add(makeTriangle(2, 6, 3));
                triangles.add(makeTriangle(3, 6, 7));
            }
            if (!dirHasBit(dir, BIT_NEG_Y)){
                triangles.add(makeTriangle(0, 1, 4));
                triangles.add(makeTriangle(4, 1, 5));
            }
            if (!dirHasBit(dir, BIT_POS_Z)){
                triangles.add(makeTriangle(1, 3, 5));
                triangles.add(makeTriangle(5, 3, 7));
            }
            if (!dirHasBit(dir, BIT_NEG_Z)){
                triangles.add(makeTriangle(0, 4, 2));
                triangles.add(makeTriangle(2, 4, 6));
            }
            return triangles.toArray(new Triangle[0]);
        }
    }

    public static class CubeOut extends AbsCube {
        int dir;
        public CubeOut(Vec3f posAng, Vec3f negAng, int dir) {
            super(posAng, negAng);
            this.dir = dir;
        }

        @Override
        Triangle[] getTriangles() {
            ArrayList<Triangle> triangles = new ArrayList<>();
            if (!dirHasBit(dir, BIT_POS_X)){
                triangles.add(makeTriangle(4, 6, 5));
                triangles.add(makeTriangle(5, 6, 7));
            }
            if (!dirHasBit(dir, BIT_NEG_X)){
                triangles.add(makeTriangle(0, 1, 2));
                triangles.add(makeTriangle(2, 1, 3));
            }
            if (!dirHasBit(dir, BIT_POS_Y)){
                triangles.add(makeTriangle(2, 3, 6));
                triangles.add(makeTriangle(6, 3, 7));
            }
            if (!dirHasBit(dir, BIT_NEG_Y)){
                triangles.add(makeTriangle(0, 4, 1));
                triangles.add(makeTriangle(1, 4, 5));
            }
            if (!dirHasBit(dir, BIT_POS_Z)){
                triangles.add(makeTriangle(1, 5, 3));
                triangles.add(makeTriangle(3, 5, 7));
            }
            if (!dirHasBit(dir, BIT_NEG_Z)){
                triangles.add(makeTriangle(0, 2, 4));
                triangles.add(makeTriangle(4, 2, 6));
            }
            return triangles.toArray(new Triangle[0]);
        }
    }

    static boolean dirHasBit(int dir, int bit){
        return (dir & bit) == bit;
    }
}
