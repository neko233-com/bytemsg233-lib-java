package com.neko233.bytemsg233;

public enum ByteMsgWireType {
    VARINT(0),
    BYTES(2);

    private final int value;

    ByteMsgWireType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
