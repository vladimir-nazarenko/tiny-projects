package com.nazarenko;

/**
 * Created by Vladimir Nazarenko on 7/30/16.
 */
public class RowWiseMultiplierTest extends MultiplierTest {
    @Override
    protected Multiplier getMultiplier() {
        return new RowWiseMultiplier();
    }
}