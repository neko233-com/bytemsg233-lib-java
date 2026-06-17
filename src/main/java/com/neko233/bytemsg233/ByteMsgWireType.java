package com.neko233.bytemsg233;

public enum ByteMsgWireType {
    VARINT(0),
    FIXED64(1),
    BYTES(2),
    LENGTH_DELIMITED(2),
    FIXED32(5);

    private final int value;

    ByteMsgWireType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ByteMsgWireType fromValue(int value) {
        switch (value) {
            case 0:
                return VARINT;
            case 1:
                return FIXED64;
            case 2:
                return BYTES;
            case 5:
                return FIXED32;
            default:
                throw new IllegalArgumentException("Unsupported ByteMsg233 wire type: " + value);
        }
    }
}
