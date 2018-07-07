package nktl.dwarf.cubes;

import nktl.math.geom.Vec3i;
import nktl.math.graph.Graph;

public class BaseCube {
    private Vec3i position;
    private Graph<BaseCube>.Node node;

    BaseCube(Vec3i pos){
        position = pos;
    }

    public void setNode(Graph<BaseCube>.Node node){
        this.node = node;
    }
    public Graph<BaseCube>.Node getNode(){
        return node;
    }
    public Vec3i position(){
        return position;
    }
    public double distanceTo(Vec3i pos){
        return new Vec3i(
                position.x - pos.x,
                position.y - pos.y,
                position.z - pos.z).length();
    }

    public boolean is_fat(){
        return false;
    }
}
