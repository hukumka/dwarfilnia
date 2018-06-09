package nktl.writer.blocks;

import nktl.math.geom.Vec3i;
import nktl.server.MinecraftRMIProcess;

import java.io.IOException;

public interface DwarfBlock {
    public void placeAt(MinecraftRMIProcess process, Vec3i position) throws IOException;
}
