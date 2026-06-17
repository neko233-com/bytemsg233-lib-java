package com.neko233.bytemsg233;

public final class ByteMsgProtocolHello {
    private final long version;
    private final long minCompatible;

    public ByteMsgProtocolHello(long version, long minCompatible) {
        this.version = version;
        this.minCompatible = minCompatible;
    }

    public long getVersion() {
        return version;
    }

    public long getMinCompatible() {
        return minCompatible;
    }
}
