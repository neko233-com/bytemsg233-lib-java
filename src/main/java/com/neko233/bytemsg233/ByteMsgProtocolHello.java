package com.neko233.bytemsg233;

public final class ByteMsgProtocolHello {
    private final long version;
    private final long fingerprint;
    private final long minCompatible;

    public ByteMsgProtocolHello(long version, long fingerprint, long minCompatible) {
        this.version = version;
        this.fingerprint = fingerprint;
        this.minCompatible = minCompatible;
    }

    public long getVersion() {
        return version;
    }

    public long getFingerprint() {
        return fingerprint;
    }

    public long getMinCompatible() {
        return minCompatible;
    }
}
