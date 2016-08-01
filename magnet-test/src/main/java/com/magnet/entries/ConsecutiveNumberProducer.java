package com.magnet.entries;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by vladimir on 7/16/16.
 */
public final class ConsecutiveNumberProducer implements NumberProducer {
    private final AtomicInteger counter = new AtomicInteger();

    private int limit;

    @Override
    public int getNextInt() {
        return counter.incrementAndGet();
    }

    @Override
    public boolean hasNext() {
        return counter.get() < limit;
    }

    @Override
    public void close() {}

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getLimit() {
        return limit;
    }
}
