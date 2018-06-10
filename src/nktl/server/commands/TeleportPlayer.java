package nktl.server.commands;

import nktl.math.geom.Vec3i;

public class TeleportPlayer extends Command{
    private String playerName;
    private String toPlayer = null;
    private Vec3i toPosition = null;

    public TeleportPlayer(String name){
        playerName = name;
    }

    public TeleportPlayer toPlayer(String name){
        toPlayer = name;
        toPosition = null;
        return this;
    }

    public TeleportPlayer toPosition(Vec3i pos){
        toPosition = pos;
        toPlayer = null;
        return this;
    }

    @Override
    public String toCommandString(){
        if(toPlayer != null) {
            return String.format("tp %s %s", playerName, toPlayer);
        }else{
            return String.format(
                    "tp %s %d %d %d",
                    playerName,
                    toPosition.x, toPosition.y, toPosition.z
            );
        }
    }
}
