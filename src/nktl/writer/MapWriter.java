package nktl.writer;

import nktl.dwarf.DwarfCube;
import nktl.dwarf.DwarfGen;
import nktl.dwarf.DwarfMap;
import nktl.math.geom.Vec3i;
import nktl.server.MinecraftRMIProcess;
import nktl.server.commands.TeleportPlayer;
import nktl.writer.blocks.Corridor;
import nktl.writer.blocks.DwarfBlock;
import nktl.writer.blocks.Stairs;
import nktl.writer.blocks.VerticalLadder;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

public class MapWriter {
    private Vec3i offset = null;
    private MinecraftRMIProcess process = null;
    private String userPlayerName = null;
    private int sleepBetweenChunks = 500;
    private int sleepBetweenCubes = 70;

    public MapWriter setProcess(MinecraftRMIProcess process){
        this.process = process;
        return this;
    }

    public MapWriter setUsedPlayer(String name){
        userPlayerName = name;
        return this;
    }

    public MapWriter setSleepBetweenChunks(int milliseconds){
        sleepBetweenChunks = milliseconds;
        return this;
    }

    public MapWriter setSleepBetweenCubes(int milliseconds){
        sleepBetweenCubes = milliseconds;
        return this;
    }

    public void writeMap(DwarfMap map) throws IOException {
        try{
            HashMap<DwarfCube.CubeType, LinkedList<DwarfCube>> cubes = DwarfCube.separate(map.getCubes());
            LinkedList[] order = {
                    cubes.get(DwarfCube.CubeType.TUNNEL),
                    cubes.get(DwarfCube.CubeType.LADDER),
                    cubes.get(DwarfCube.CubeType.COLLECTOR),
                    cubes.get(DwarfCube.CubeType.STAIRS),
            };
            for(LinkedList t: order){
                t = (LinkedList<DwarfCube>) t;
                if(t != null){
                    ChuckMap m = new ChuckMap(t);
                    for (ChuckMap.Chunk c : m) {
                        writeChunk(c);
                    }
                }
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }


    private void writeChunk(ChuckMap.Chunk chunk) throws IOException, InterruptedException{
        System.out.println("Writing chunk " +  chunk.getId().toString());
        Vec3i center = mapToWorld(chunk.getCenter());
        if(userPlayerName != null){
            new TeleportPlayer(userPlayerName)
                    .toPosition(center)
                    .runIn(process);
        }
        if(sleepBetweenChunks > 0) {
            Thread.sleep(sleepBetweenChunks);
        }
        for(DwarfCube c: chunk.list()){
            DwarfBlock block = DwarfBlock.from_dwarf_cube(c);
            if(block != null){
                block.placeAt(process, mapToWorld(c.position()));

                if(sleepBetweenCubes > 0) {
                    Thread.sleep(sleepBetweenCubes);
                }
            }
        }
    }

    public MapWriter setOffset(Vec3i offset){
        this.offset = offset;
        return this;
    }

    private Vec3i mapToWorld(Vec3i mapCoordinates){
        return offset.plus(mapCoordinates.x*5, mapCoordinates.y*5, mapCoordinates.z*5);
    }

}
