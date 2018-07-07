package nktl.dwarf.graphics;

public class Triangle {
    private Vertex[] vs;

    public Triangle(Vertex v1, Vertex v2, Vertex v3){
        vs = new Vertex[] {
                v1, v2, v3
        };
    }

    public Vertex getV1() { return vs[0]; }
    public Vertex getV2() { return vs[1]; }
    public Vertex getV3() { return vs[2]; }
    public Vertex[] getVs() { return vs; }
}
