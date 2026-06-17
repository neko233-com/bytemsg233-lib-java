package com.neko233.bytemsg233;

public final class ByteMsgProtocol {
    private ByteMsgProtocol() {}

    public static byte[] writeHello(ByteMsgProtocolHello hello) {
        ByteMsgWriter writer = new ByteMsgWriter();
        writer.writeFieldHeader(1, ByteMsgWireType.VARINT);
        writer.writeVarint(hello.getVersion());
        writer.writeFieldHeader(2, ByteMsgWireType.VARINT);
        writer.writeVarint(hello.getFingerprint());
        writer.writeFieldHeader(3, ByteMsgWireType.VARINT);
        writer.writeVarint(hello.getMinCompatible());
        return writer.toByteArray();
    }

    public static ByteMsgProtocolHello readHello(byte[] data) {
        ByteMsgReader reader = new ByteMsgReader(data);
        long version = 0;
        long fingerprint = 0;
        long minCompatible = 0;
        while (!reader.isEof()) {
            ByteMsgFieldHeader header = reader.readFieldHeader();
            switch (header.getTag()) {
                case 1:
                    version = reader.readVarint();
                    break;
                case 2:
                    fingerprint = reader.readVarint();
                    break;
                case 3:
                    minCompatible = reader.readVarint();
                    break;
                default:
                    reader.skipField(header.getWireType());
                    break;
            }
        }
        return new ByteMsgProtocolHello(version, fingerprint, minCompatible);
    }

    public static void checkCompatible(ByteMsgProtocolHello local, ByteMsgProtocolHello remote) {
        if (local.getFingerprint() != 0 && remote.getFingerprint() != 0 && local.getFingerprint() != remote.getFingerprint()) {
            throw new IllegalStateException("ByteMsg233 protocol fingerprint mismatch.");
        }
        if (Long.compareUnsigned(remote.getVersion(), local.getMinCompatible()) < 0
            || Long.compareUnsigned(local.getVersion(), remote.getMinCompatible()) < 0) {
            throw new IllegalStateException("ByteMsg233 protocol version mismatch.");
        }
    }
}
