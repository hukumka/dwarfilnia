package nktl.server.commands;

import java.util.ArrayList;

public class BlockData {
    String name;
    ArrayList<BlockParam> params;

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
            builder.append('[');
            for(BlockParam param: params){
                builder.append(param.getParamString());
            }
            builder.append(']');
            return builder.toString();
        }
    }

    public BlockData rotate90Y(){
        for(int i=0; i<params.size(); ++i){
            params.set(i, params.get(i).rotate90Y());
        }
        return this;
    }
}
