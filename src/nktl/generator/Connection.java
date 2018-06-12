package nktl.generator;

import nktl.math.graph.Graph;

public class Connection {
    private Graph<DwarfCube>.Node node1;
    private Graph<DwarfCube>.Node node2;
    private DwarfCube cube;

    Connection(Graph<DwarfCube>.Node node1, Graph<DwarfCube>.Node node2, DwarfCube cube){
        this.node1 = node1;
        this.node2 = node2;
        this.cube = cube;
    }

    public Graph<DwarfCube>.Node getNode1() {
        return node1;
    }

    public Graph<DwarfCube>.Node getNode2() {
        return node2;
    }

    public boolean hasNode(Graph<DwarfCube>.Node node){
        return node1 == node || node2 == node;
    }

    public DwarfCube getCube() {
        return cube;
    }
}
