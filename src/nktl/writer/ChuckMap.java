package nktl.writer;

import nktl.generator.DwarfCube;
import nktl.generator.DwarfMap;
import nktl.math.geom.Vec3i;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ChuckMap implements Iterable<ChuckMap.Chunk>{
    class Chunk{
        private ArrayList<DwarfCube> cubes;
        private Vec3i chunkId;

        public Chunk(Vec3i id){
            cubes = new ArrayList<>();
            chunkId = id;
        }

        void put(DwarfCube cube){
            cubes.add(cube);
        }

        public ArrayList<DwarfCube> list(){
            return cubes;
        }

        public Vec3i getCenter(){
            return chunkId.mult(chunkSize).plus(chunkSize/2, chunkSize/2, chunkSize/2);
        }
        public Vec3i getId(){
            return chunkId;
        }
    }

    static int chunkSize = 16;

    HashMap<Vec3i, Chunk> chucks;

    public ChuckMap(DwarfMap map){
        chucks = new HashMap<>();
        for(DwarfCube c: map.toCubeList()){
            addCube(c);
        }
    }

    void addCube(DwarfCube c){
        Vec3i pos = c.getPosition();
        Vec3i chunkId = new Vec3i(pos.x/chunkSize, pos.y/chunkSize, pos.z/chunkSize);
        Chunk chunk = chucks.get(chunkId);
        if(chunk == null){
            chunk = new Chunk(chunkId);
            chucks.put(chunkId, chunk);
        }
        chunk.put(c);
    }


    @Override
    public Iterator<Chunk> iterator(){
        return chucks.values().iterator();
    }

}
