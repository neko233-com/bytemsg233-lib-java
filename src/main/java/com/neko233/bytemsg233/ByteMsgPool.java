package com.neko233.bytemsg233;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Supplier;

public final class ByteMsgPool<T extends ByteMsgResettable> {
    private final ConcurrentLinkedQueue<T> items = new ConcurrentLinkedQueue<>();
    private final Supplier<T> factory;

    public ByteMsgPool(Supplier<T> factory) {
        this.factory = factory;
    }

    public T acquire() {
        T value = items.poll();
        return value != null ? value : factory.get();
    }

    public void release(T value) {
        if (value == null) {
            return;
        }
        value.reset();
        items.offer(value);
    }
}
