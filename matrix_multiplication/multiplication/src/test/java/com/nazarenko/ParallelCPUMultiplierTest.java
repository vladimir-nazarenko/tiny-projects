package com.nazarenko;

/**
 * Created by Vladimir Nazarenko on 8/1/16.
 */
public class ParallelCPUMultiplierTest extends MultiplierTest {
    @Override
    protected Multiplier getMultiplier() {
        return new ParallelCPUMultiplier();
    }
}
