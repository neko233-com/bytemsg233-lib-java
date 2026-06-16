package com.neko233.bytemsg233;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Supplier;

public final class ByteMsgPool<T extends ByteMsgResettable> {
    private final Deque<T> items = new ArrayDeque<>();
    private final Supplier<T> factory;

    public ByteMsgPool(Supplier<T> factory) {
        this.factory = factory;
    }

    public T acquire() {
        return items.isEmpty() ? factory.get() : items.pop();
    }

    public void release(T value) {
        if (value == null) {
            return;
        }
        value.reset();
        items.push(value);
    }
}
