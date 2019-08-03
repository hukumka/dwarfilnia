package nktl.server.commands;

import java.util.ArrayList;

public class BlockData {
    String name;
    ArrayList<BlockParam> params = new ArrayList<>();

    public BlockData(String name){
        this.name = name;
    }

    public BlockData addParam(BlockParam param){
        params.add(param);
        return this;
    }

    public String command(){
        if(params.isEmpty()){
            return name;
        }else{
            StringBuilder builder = new StringBuilder();
            builder.append(name);

            var iter = params.iterator();
            builder.append('[');
            builder.append(iter.next().getParamString());
            while (iter.hasNext()){
                builder.append(',').append(iter.next().getParamString());
            }
            builder.append(']');
            return builder.toString();
        }
    }

    public BlockData rotate90Y(){

        BlockData bd = new BlockData(name);
        for (var param : params)
            bd.params.add(param.rotate90Y());

        return bd;
    }


}
