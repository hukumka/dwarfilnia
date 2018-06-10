package nktl.writer;

import nktl.generator.DwarfCube;
import nktl.generator.DwarfList;
import nktl.generator.DwarfMap;
import nktl.math.geom.Vec3i;
import nktl.server.MinecraftRMIProcess;
import nktl.server.commands.TeleportPlayer;
import nktl.writer.blocks.Corridor;
import nktl.writer.blocks.DwarfBlock;
import nktl.writer.blocks.VerticalLadder;

import java.io.IOException;

public class MapWriter {
    private Vec3i offset = null;
    private MinecraftRMIProcess process = null;
    private String userPlayerName = null;
    private int sleepBetweenChunks = 1000;
    private int sleepBetweenCubes = 50;

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
            DwarfList list = map.toCubeList();
            ChuckMap cm = new ChuckMap(list.get5x5());
            for(ChuckMap.Chunk c: cm){
                writeChunk(c);
            }
            cm = new ChuckMap(list.get7x7());
            for(ChuckMap.Chunk c: cm){
                writeChunk(c);
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
            DwarfBlock block = null;
            switch (c.getType()){
                case DwarfCube.TYPE_TUNNEL:
                    block = new Corridor().setData(c.getDirection());
                    break;
                case DwarfCube.TYPE_VERTICAL_LADDER:
                    block = new VerticalLadder()
                            .setWays(c.getDirection());
                    break;
            }
            if(block != null){
                block.placeAt(process, mapToWorld(c.getPosition()));

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
        return offset.plus(mapCoordinates.x*5, mapCoordinates.z*5, mapCoordinates.y*5);
    }

}
