package nktl.writer.blocks;

import nktl.math.geom.Vec3i;
import nktl.server.MinecraftRMIProcess;
import nktl.server.commands.Command;
import nktl.server.commands.Fill;

import java.io.IOException;

public class Corridor implements DwarfBlock{
    @Override
    public void placeAt(MinecraftRMIProcess process, Vec3i position) throws IOException{
        Command[] commands = {
                // fill all with air
                new Fill(position, position.add(4, 4, 4), "minecraft:air"),
                // create floor
                new Fill(position, position.add(4, 0, 4), "minecraft:stonebrick"),
                // create ceiling
                new Fill(position.add(0, 4, 0), position.add(4, 4, 4), "minecraft:stonebrick"),
        };
        for(var c: commands){
            process.write(c.toCommandString());
        }
    }
}
