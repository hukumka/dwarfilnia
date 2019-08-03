package nktl.server.commands.states;

import nktl.server.commands.BlockParam;

public enum SlabType implements BlockParam {
    bottom, top;

    @Override
    public SlabType rotate90Y() { return this; }

    @Override
    public String getParamString() {
        return "type="+this;
    }
}
