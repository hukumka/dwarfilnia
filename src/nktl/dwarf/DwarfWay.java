package nktl.dwarf;


import nktl.math.geom.Vec3i;
import nktl.math.graph.Graph;

class DwarfWay extends Vec3i {

    static final int
            DIR_POS_X = 1,
            DIR_POS_Z = 2,
            DIR_NEG_X = 3,
            DIR_NEG_Z = 4,
            DIR_POS_Y_UP = 5,
            DIR_NEG_Y_DOWN = 6;

    static final int
            TUNNEL_TYPE_DEFAULT = 0,
            TUNNEL_TYPE_DIAGONAL_LADDER_UP = 1,
            TUNNEL_TYPE_DIAGONAL_LADDER_DOWN = 2;

    private Attractor attractor = null;
    private Graph<DwarfCube>.Node node;
    private int dir, type;
    private boolean canBeContinued = true;
    private boolean onWay = false;


    DwarfWay(Graph<DwarfCube>.Node node, Vec3i pos, int dir){
        this.copy(pos);
        this.node = node;
        this.dir = dir;
    }

    DwarfWay[] genWays(DwarfMap map, DwarfSet set) throws GeneratorException {
        if (!canBeContinued) return new DwarfWay[0];
        return new DwarfWay[0];
    }

    void drawWay(DwarfMap map, DwarfSet set) throws GeneratorException {
        Vec3i increment = new Vec3i();
        Vec3i decrement = new Vec3i();
        switch (dir) {
            case DIR_POS_X: increment.x = 1; break;
            case DIR_NEG_X: increment.x = -1; break;
            case DIR_POS_Z: increment.z = 1; break;
            case DIR_NEG_Z: increment.z = -1; break;
            case DIR_POS_Y_UP: increment.y = 1; break;
            case DIR_NEG_Y_DOWN: increment.y = -1; break;
            default: throw new GeneratorException("Cannot draw the Way with direction = 0");
        }
        decrement.x = -increment.x;
        decrement.y = -increment.y;
        decrement.z = -increment.z;
        int length = (int) (set.min_tunnel_len + Math.round(set.random()*set.delta_tunnel_len));
        if (map.getCubeAt(this).isFat())
            if(length < 2) length = 2;
        if (type == TUNNEL_TYPE_DIAGONAL_LADDER_UP)
            drawDiagUp(map, set, length, increment, decrement);
        else if (type == TUNNEL_TYPE_DIAGONAL_LADDER_DOWN)
            drawDiagDown(map, set, length, increment, decrement);
        else
            drawTunnel(map, set, length, increment, decrement);
    }

    private void drawTunnel(DwarfMap map, DwarfSet set, int length, Vec3i increment, Vec3i decrement) {

    }

    private void drawDiagDown(DwarfMap map, DwarfSet set, int length, Vec3i increment, Vec3i decrement) {

    }

    private void drawDiagUp(DwarfMap map, DwarfSet set, int length, Vec3i increment, Vec3i decrement) {

    }


    boolean fatAtDiagonal(DwarfMap map, Vec3i pos){
        Vec3i[]ps = {
                new Vec3i(pos), new Vec3i(pos),
                new Vec3i(pos), new Vec3i(pos)
        };
        ++ps[0].x; ++ps[0].z;
        ++ps[1].x; --ps[1].z;
        --ps[2].x; ++ps[2].z;
        --ps[3].x; --ps[3].z;
        for (var p : ps){
            if (map.hasPosition(p) && map.getCubeAt(p).isFat())
                return false;
        }
        return true;
    }

    void setAttractor(Attractor attractor){
        this.attractor = attractor;
    }

    void setType(int type){
        this.type = type;
    }
}
