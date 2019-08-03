package nktl.server.commands.states;

import nktl.server.commands.BlockParam;

public enum Half implements BlockParam {
    bottom, top;

    @Override
    public Half rotate90Y() { return this; }

    @Override
    public String getParamString() {
        return "half="+this;
    }
}
