package com.magnet.entries;

import java.io.Closeable;

/**
 * Created by vladimir on 7/16/16.
 */
public interface NumberProducer extends Closeable {
    int getNextInt();

    boolean hasNext();
}
