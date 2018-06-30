package nktl.writer.tests;

import nktl.generator.DwarfCube;
import nktl.generator.DwarfMap;
import nktl.math.geom.Direction;
import nktl.math.geom.Vec3i;
import nktl.server.MinecraftRMIProcess;
import nktl.writer.MapWriter;
import nktl.writer.RMIClient;
import nktl.writer.blocks.*;


public class TestBlocks {
    public static void main(String[] args){
        for(int i=0; i<0xf; ++i){
            System.out.println(String.format("%x %x", i, rotateDirection(i)));
        }
        try {
            MinecraftRMIProcess process = new RMIClient().getProcess();
            DwarfMap map = new DwarfMap(new Vec3i(10, 10, 10));
            Vec3i corner = new Vec3i(0, 0, 0);
            Vec3i direction = new Vec3i(1, 0, 0);
            for(int d=0; d<4; ++d){
                for(int i=0; i<4; ++i){
                    for(int h=0; h<10; ++h){
                        Vec3i pos = corner
                                .plus(direction.mult(i))
                                .plus(0, 0, h);
                        switch (i){
                            case 0:
                                map.createCubeAt(pos)
                                        .setDirection(rotateDirectionN(DwarfCube.DIRECTION_EAST_BIT | DwarfCube.DIRECTION_NORTH_BIT, d))
                                        .setType(DwarfCube.TYPE_TUNNEL);
                                break;
                            case 2:
                                map.createCubeAt(pos)
                                        .setDirection(rotateDirectionN(DwarfCube.DIRECTION_WEST_BIT | DwarfCube.DIRECTION_EAST_BIT | DwarfCube.DIRECTION_NORTH_BIT, d))
                                        .setType(DwarfCube.TYPE_TUNNEL);
                                break;
                            default:
                                map.createCubeAt(pos)
                                        .setDirection(rotateDirectionN(DwarfCube.DIRECTION_EAST_BIT, d))
                                        .setType(DwarfCube.TYPE_DIAGONAL_LADDER);
                        }
                        if(h>1 && h<5 && i==3){
                            System.out.println(String.format("%d %s", d, pos.toString()));
                        }
                    }
                }
                corner = new Vec3i(corner.y, 4-corner.x, 0);
                direction = new Vec3i(direction.y, -direction.x, 0);
            }

            new MapWriter()
                    .setProcess(process)
                    .setOffset(new Vec3i(-200, 200, -100))
                    .setUsedPlayer("hukumka")
                    .writeMap(map);

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    static int rotateDirection(int dirBits){
        // clockwise rotation
        dirBits *= 0x11111;
        dirBits &= 0x30084;
        dirBits *= 0x20c1;
        dirBits >>= 0xe;
        return dirBits & 0xf;
    }

    static int rotateDirectionN(int dirBits, int count){
        for(int i=0; i<count; ++i){
            dirBits = rotateDirection(dirBits);
        }
        return dirBits;
    }
}
