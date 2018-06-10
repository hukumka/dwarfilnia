package nktl.writer;

import nktl.generator.DwarfCube;
import nktl.generator.DwarfMap;
import nktl.math.geom.Vec3i;
import nktl.server.MinecraftRMIProcess;
import nktl.writer.blocks.Corridor;
import nktl.writer.blocks.DwarfBlock;

import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;

public class MapWriter {
    Vec3i offset = null;
    MinecraftRMIProcess process = null;

    Deque<DwarfCube> cubes = new LinkedList<>();

    public MapWriter setProcess(MinecraftRMIProcess process){
        this.process = process;
        return this;
    }

    public void writeMap(DwarfMap map) throws IOException {
        for(DwarfCube cube: map.toCubeList()){
            Vec3i worldPos = mapToWorld(cube.getPosition());
            DwarfBlock block = null;
            switch (cube.getType()){
                case 0:
                    block = new Corridor();
            }
            if(block != null){
                block.placeAt(process, worldPos);
            }
            try {
                Thread.sleep(50);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public MapWriter setOffset(Vec3i offset){
        this.offset = offset;
        return this;
    }

    Vec3i mapToWorld(Vec3i mapCoordinates){
        return offset.plus(mapCoordinates.x*5, mapCoordinates.z*5, mapCoordinates.y*5);
    }

}
