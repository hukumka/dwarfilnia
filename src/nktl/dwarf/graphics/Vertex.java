package nktl.dwarf.graphics;


import nktl.GL4.util.Vec3f;

import java.util.Objects;

public class Vertex extends Vec3f {
    public int index = 0;

    public Vertex(float x, float y, float z) {
        super(x, y, z);
    }

    public Vertex(double x, double y, double z) {
        super((float) x, (float) y, (float) z);
    }

    public Vertex(float x, float y, float z, int index) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.index = index;
    }

    public void turn90(){
        float temp = x;
        x = -z;
        z = temp;
    }
    public void turn180(){
        x = -x;
        z = -z;
    }
    public void turn270(){
        float temp = x;
        x = z;
        z = -temp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Vertex vertex = (Vertex) o;
        return index == vertex.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), index);
    }
}
