package com.nazarenko;

/**
 * Created by Vladimir Nazarenko on 8/3/16.
 */
public class IKJMultiplierTest extends MultiplierTest {
    @Override
    protected Multiplier getMultiplier() {
        return new IKJMultiplier();
    }
}
