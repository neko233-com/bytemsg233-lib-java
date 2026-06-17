package com.neko233.bytemsg233;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public final class ByteMsgWriter {
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();

    public byte[] toByteArray() {
        return out.toByteArray();
    }

    public void reset() {
        out.reset();
    }

    public void writeVarint(long value) {
        while ((value & ~0x7FL) != 0) {
            out.write((int) ((value & 0x7F) | 0x80));
            value >>>= 7;
        }
        out.write((int) value);
    }

    public void writeFieldHeader(int tag, ByteMsgWireType wireType) {
        if (tag <= 0) {
            throw new IllegalArgumentException("Field tag must be positive.");
        }
        writeVarint(((long) tag << 3) | wireType.getValue());
    }

    public void writeBytes(byte[] value) {
        byte[] bytes = value == null ? new byte[0] : value;
        writeVarint(bytes.length);
        out.writeBytes(bytes);
    }

    public void writeString(String value) {
        byte[] bytes = (value == null ? "" : value).getBytes(StandardCharsets.UTF_8);
        writeBytes(bytes);
    }

    public void writeFixed32(int value) {
        out.write(value & 0xFF);
        out.write((value >>> 8) & 0xFF);
        out.write((value >>> 16) & 0xFF);
        out.write((value >>> 24) & 0xFF);
    }

    public void writeFixed64(long value) {
        out.write((int) (value & 0xFF));
        out.write((int) ((value >>> 8) & 0xFF));
        out.write((int) ((value >>> 16) & 0xFF));
        out.write((int) ((value >>> 24) & 0xFF));
        out.write((int) ((value >>> 32) & 0xFF));
        out.write((int) ((value >>> 40) & 0xFF));
        out.write((int) ((value >>> 48) & 0xFF));
        out.write((int) ((value >>> 56) & 0xFF));
    }

    public void writePackedVarints(List<Long> values) {
        int count = values == null ? 0 : values.size();
        writeVarint(count);
        if (values == null) return;
        for (long value : values) {
            writeVarint(value);
        }
    }

    public void writeDeltaVarints(List<Long> values) {
        int count = values == null ? 0 : values.size();
        writeVarint(count);
        if (count == 0) return;
        long previous = values.get(0);
        writeVarint(previous);
        for (int i = 1; i < count; i++) {
            long current = values.get(i);
            writeVarint(zigZagEncode(current - previous));
            previous = current;
        }
    }

    public void writeBoolBitset(List<Boolean> values) {
        int count = values == null ? 0 : values.size();
        writeVarint(count);
        if (values == null) return;
        int current = 0;
        for (int i = 0; i < count; i++) {
            if (Boolean.TRUE.equals(values.get(i))) {
                current |= 1 << (i & 7);
            }
            if ((i & 7) == 7) {
                out.write(current);
                current = 0;
            }
        }
        if ((count & 7) != 0) {
            out.write(current);
        }
    }

    public void writeStringList(List<String> values) {
        int count = values == null ? 0 : values.size();
        writeVarint(count);
        if (values == null) return;
        for (String value : values) {
            writeString(value);
        }
    }

    public static long zigZagEncode(long value) {
        return (value << 1) ^ (value >> 63);
    }

    public static long zigZagDecode(long value) {
        return (value >>> 1) ^ -(value & 1);
    }
}
