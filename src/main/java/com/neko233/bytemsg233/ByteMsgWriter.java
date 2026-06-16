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

    public void writeString(String value) {
        byte[] bytes = (value == null ? "" : value).getBytes(StandardCharsets.UTF_8);
        writeVarint(bytes.length);
        out.writeBytes(bytes);
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
