package com.nazarenko;

/**
 * Created by Vladimir Nazarenko on 7/31/16.
 */
public class StrassenMultiplierTest extends MultiplierTest {
    @Override
    protected Multiplier getMultiplier() {
        return new StrassenMultiplier(new RowWiseMultiplier(), 64);
    }
}