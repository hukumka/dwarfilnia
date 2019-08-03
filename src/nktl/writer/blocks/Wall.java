package nktl.writer.blocks;

import nktl.math.geom.Vec3i;
import nktl.server.MinecraftRMIProcess;
import nktl.server.commands.BlockData;
import nktl.server.commands.Command;
import nktl.server.commands.Fill;

import java.io.IOException;

public class Wall implements DwarfBlock {
    @Override
    public void placeAt(MinecraftRMIProcess process, Vec3i position) throws IOException{
        Command cmd = new Fill(position, position.plus(4, 4, 4), new BlockData("minecraft:stone_bricks"))
                .outline();
        process.write(cmd.toCommandString());
    }
}
