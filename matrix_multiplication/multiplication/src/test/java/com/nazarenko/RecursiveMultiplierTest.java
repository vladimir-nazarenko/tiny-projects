package com.nazarenko;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Vladimir Nazarenko on 7/30/16.
 */
public class RecursiveMultiplierTest {

    private Multiplier multiplier;
    private TestSetup setup;

    @Before
    public void setUp() {
        setup = new TestSetup();
        setup.initMatrices();
        multiplier = new RowWiseMultiplier();
    }

    @After
    public void tearDown() {
        setup.invalidateMatrices();
    }

    @Test
    public void testMultiply() throws Exception {
        final TestSetup local_setup = setup;
        final int[][] actual = multiplier.multiply(local_setup.getProvider().getLeftMatrix(),
                local_setup.getProvider().getRightMatrix());
        for (int i = 0; i < local_setup.getM(); i++)
            for (int j = 0; j < local_setup.getK(); j++)
                assertEquals(
                        String.format("%d expected at (%d, %d), found %d",
                                local_setup.getReference()[i][j], i, j, actual[i][j]),
                        local_setup.getReference()[i][j], actual[i][j]);
    }
}