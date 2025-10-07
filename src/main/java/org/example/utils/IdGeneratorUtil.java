package org.example.utils;

import java.util.concurrent.atomic.AtomicLong;

public class IdGeneratorUtil {
    private static final AtomicLong counter = new AtomicLong();

    public static long nextId() {
        return counter.incrementAndGet();
    }

    public static long getId() {
        return counter.get();
    }
}
