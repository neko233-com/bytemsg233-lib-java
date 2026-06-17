package com.neko233.bytemsg233;

public final class ByteMsgFieldHeader {
    private final int tag;
    private final ByteMsgWireType wireType;

    public ByteMsgFieldHeader(int tag, ByteMsgWireType wireType) {
        this.tag = tag;
        this.wireType = wireType;
    }

    public int getTag() {
        return tag;
    }

    public ByteMsgWireType getWireType() {
        return wireType;
    }
}
