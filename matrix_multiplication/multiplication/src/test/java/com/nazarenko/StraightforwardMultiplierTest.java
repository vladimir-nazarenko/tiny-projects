package com.nazarenko;

/**
 * Created by Vladimir Nazarenko on 8/1/16.
 */
public class StraightforwardMultiplierTest extends MultiplierTest{
    @Override
    protected Multiplier getMultiplier() {
        return new StraightforwardMultiplier();
    }
}
