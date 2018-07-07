package nktl.dwarf.graphics;

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
    public Cube(int indexOffset, Vec3i pos){
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
            System.out.println("Add positive X");
        }
        if (!dirHasBit(BIT_NEG_X)){
            triangles.add(makeTriangle(0, 2, 1));
            triangles.add(makeTriangle(1, 2, 3));
            System.out.println("Add negative X");
        }
        if (!dirHasBit(BIT_POS_Y)){
            triangles.add(makeTriangle(2, 6, 3));
            triangles.add(makeTriangle(3, 6, 7));
            System.out.println("Add positive Y");
        }
        if (!dirHasBit(BIT_NEG_Y)){
            triangles.add(makeTriangle(0, 1, 4));
            triangles.add(makeTriangle(4, 1, 5));
            System.out.println("Add negative Y");
        }
        if (!dirHasBit(BIT_POS_Z)){
            triangles.add(makeTriangle(1, 3, 5));
            triangles.add(makeTriangle(5, 3, 7));
            System.out.println("Add positive Z");
        }
        if (!dirHasBit(BIT_NEG_Z)){
            triangles.add(makeTriangle(0, 4, 2));
            triangles.add(makeTriangle(2, 4, 6));
            System.out.println("Add negative Z");
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
    public static Vec3i zxy(Vec3i src){
        return new Vec3i(src.y, src.z, src.x);
    }

}
