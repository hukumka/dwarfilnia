package nktl.server.commands;

import java.util.ArrayList;

interface State{
    public String name();
    public String value();
    public State rotate90Y();
}

public class BlockData {
    String name;
    ArrayList<State> params;

    public BlockData(String name){
        this.name = name;
    }

    public BlockData addParam(State param){
        params.add(param);
        return this;
    }

    public String command(){
        if(params.isEmpty()){
            return name;
        }else{
            StringBuilder builder = new StringBuilder();
            builder.append(name);
            builder.append('{');
            for(State param: params){
                builder.append(param.name());
                builder.append(": ");
                builder.append(param.value());
            }
            builder.append('}');
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
