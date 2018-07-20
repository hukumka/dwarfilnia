package nktl.dwarf.graphics;

import nktl.dwarf.DwarfCube;
import nktl.math.geom.Vec3i;

import java.util.ArrayList;

public class Cube {

    /*
        Константы
     */
    public static final int
            BIT_POS_X = 0b1,
            BIT_POS_Z = 0b10,
            BIT_NEG_X = 0b100,
            BIT_NEG_Z = 0b1000,
            BIT_POS_Y = 0b10000,
            BIT_NEG_Y = 0b100000;

    /*
        Переменные объекта
     */
    private Vertex[] vertices = {
            new Vertex(-.5f,-.5f,-.5f),
            new Vertex(-.5f,-.5f,+.5f),
            new Vertex(-.5f,+.5f,-.5f),
            new Vertex(-.5f,+.5f,+.5f),
            new Vertex(+.5f,-.5f,-.5f),
            new Vertex(+.5f,-.5f,+.5f),
            new Vertex(+.5f,+.5f,-.5f),
            new Vertex(+.5f,+.5f,+.5f)
    };

    private int direction = 0;

    /*
        PUBLIC
     */
    private Cube(int indexOffset, Vec3i pos){
        for (int i = 0; i < vertices.length; i++) {
            vertices[i].index = indexOffset + i;
            vertices[i].plusIn(pos);
        }
    }

    public Vertex[] getVertices(){
        return vertices;
    }

    public void addDirectionBits(int bits) {
        direction |= bits;
    }

    public int getDirection() {
        return direction;
    }

    public boolean dirHasBit(int bit){
        return (direction & bit) > 0;
    }

    public Triangle[] getTriangles(){
        ArrayList<Triangle> triangles = new ArrayList<>();
        if (!dirHasBit(BIT_POS_X)){
            triangles.add(makeTriangle(4, 5, 6));
            triangles.add(makeTriangle(6, 5, 7));
            //System.out.println("Add positive X");
        }
        if (!dirHasBit(BIT_NEG_X)){
            triangles.add(makeTriangle(0, 2, 1));
            triangles.add(makeTriangle(1, 2, 3));
            //System.out.println("Add negative X");
        }
        if (!dirHasBit(BIT_POS_Y)){
            triangles.add(makeTriangle(2, 6, 3));
            triangles.add(makeTriangle(3, 6, 7));
            //System.out.println("Add positive Y");
        }
        if (!dirHasBit(BIT_NEG_Y)){
            triangles.add(makeTriangle(0, 1, 4));
            triangles.add(makeTriangle(4, 1, 5));
            //System.out.println("Add negative Y");
        }
        if (!dirHasBit(BIT_POS_Z)){
            triangles.add(makeTriangle(1, 3, 5));
            triangles.add(makeTriangle(5, 3, 7));
            //System.out.println("Add positive Z");
        }
        if (!dirHasBit(BIT_NEG_Z)){
            triangles.add(makeTriangle(0, 4, 2));
            triangles.add(makeTriangle(2, 4, 6));
            //System.out.println("Add negative Z");
        }
        return triangles.toArray(new Triangle[0]);
    }

    /*
        PRIVATE
     */
    private Triangle makeTriangle(int ind1, int ind2, int ind3){
        return new Triangle(vertices[ind1], vertices[ind2], vertices[ind3]);
    }



    /*
        STATIC
     */
    public static Cube make(int indexOffset, Vec3i pos, DwarfCube cube){
        switch (cube.type()){
            case LADDER:
                return new Ladder(indexOffset, pos, cube);
            default:
                return new Cube(indexOffset, pos);
        }
    }


    public static Vec3i zxy(Vec3i src){
        return new Vec3i(src.y, src.z, src.x);
    }


    public static class Ladder extends Cube {
        static double d = 0.5/6;
        Vertex[] lvs = {
                new Vertex( 0.33, 5*d, 0),
                new Vertex( 0.33, 3*d, 0),
                new Vertex(-0.33, 5*d, 0),
                new Vertex(-0.33, 3*d, 0),
                new Vertex( 0.33,  d, 0),
                new Vertex( 0.33, -d, 0),
                new Vertex(-0.33,  d, 0),
                new Vertex(-0.33, -d, 0),
                new Vertex( 0.33, -5*d, 0),
                new Vertex( 0.33, -3*d, 0),
                new Vertex(-0.33, -5*d, 0),
                new Vertex(-0.33, -3*d, 0)
        };

        private Ladder(int indexOffset, Vec3i pos, DwarfCube cube) {
            super(indexOffset, pos);
            for (Vertex v : lvs)
                v.plusIn(pos);
        }

        @Override
        public Triangle[] getTriangles() {

            Triangle[] def = super.getTriangles();
            Triangle[] out = new Triangle[def.length + 6];
            System.arraycopy(def, 0, out, 0, def.length);
            int offset = def.length;
            out[offset] = new Triangle(lvs[0], lvs[1], lvs[2]);
            out[offset+1] = new Triangle(lvs[2], lvs[1], lvs[3]);
            out[offset+2] = new Triangle(lvs[4], lvs[5], lvs[6]);
            out[offset+3] = new Triangle(lvs[6], lvs[5], lvs[7]);
            out[offset+4] = new Triangle(lvs[9], lvs[8], lvs[10]);
            out[offset+5] = new Triangle(lvs[9], lvs[10], lvs[11]);
            return out;
        }
    }

}
