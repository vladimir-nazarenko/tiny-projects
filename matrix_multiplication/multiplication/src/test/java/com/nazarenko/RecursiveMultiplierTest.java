package com.nazarenko;

/**
 * Created by Vladimir Nazarenko on 7/30/16.
 */
public class RecursiveMultiplierTest extends MultiplierTest{
    @Override
    protected Multiplier getMultiplier() {
        return new RecursiveMultiplier();
    }
}