package com.neko233.bytemsg233;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class ByteMsgReader {
    private final byte[] data;
    private int offset;

    public ByteMsgReader(byte[] data) {
        this.data = data == null ? new byte[0] : data;
    }

    public boolean isEof() {
        return offset >= data.length;
    }

    public int remaining() {
        return data.length - offset;
    }

    public int readByte() {
        if (isEof()) {
            throw new IllegalStateException("Unexpected end of ByteMsg233 buffer.");
        }
        return data[offset++] & 0xFF;
    }

    public long readVarint() {
        long result = 0;
        for (int shift = 0; shift < 64; shift += 7) {
            int current = readByte();
            result |= (long) (current & 0x7F) << shift;
            if ((current & 0x80) == 0) {
                return result;
            }
        }
        throw new IllegalArgumentException("Invalid varint encoding.");
    }

    public String readString() {
        int length = Math.toIntExact(readVarint());
        if (length > remaining()) {
            throw new IllegalStateException("ByteMsg233 length exceeds remaining buffer.");
        }
        String value = new String(data, offset, length, StandardCharsets.UTF_8);
        offset += length;
        return value;
    }

    public List<Long> readPackedVarints(List<Long> values) {
        int count = Math.toIntExact(readVarint());
        List<Long> out = values == null ? new ArrayList<>(count) : values;
        out.clear();
        for (int i = 0; i < count; i++) {
            out.add(readVarint());
        }
        return out;
    }

    public List<Long> readDeltaVarints(List<Long> values) {
        int count = Math.toIntExact(readVarint());
        List<Long> out = values == null ? new ArrayList<>(count) : values;
        out.clear();
        if (count == 0) return out;
        long current = readVarint();
        out.add(current);
        for (int i = 1; i < count; i++) {
            current += ByteMsgWriter.zigZagDecode(readVarint());
            out.add(current);
        }
        return out;
    }

    public List<Boolean> readBoolBitset(List<Boolean> values) {
        int count = Math.toIntExact(readVarint());
        List<Boolean> out = values == null ? new ArrayList<>(count) : values;
        out.clear();
        for (int i = 0; i < count; i += 8) {
            int current = readByte();
            int limit = Math.min(8, count - i);
            for (int bit = 0; bit < limit; bit++) {
                out.add((current & (1 << bit)) != 0);
            }
        }
        return out;
    }

    public List<String> readStringList(List<String> values) {
        int count = Math.toIntExact(readVarint());
        List<String> out = values == null ? new ArrayList<>(count) : values;
        out.clear();
        for (int i = 0; i < count; i++) {
            out.add(readString());
        }
        return out;
    }
}
