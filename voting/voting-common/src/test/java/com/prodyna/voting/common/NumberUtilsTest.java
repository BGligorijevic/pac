package com.prodyna.voting.common;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class NumberUtilsTest {

    private static final float INPUT_VALUE = 10.55555555f;

    /**
     * Test for {@link NumberUtils#round(float, int)}.
     */
    @Test
    public void roundsCorrectlyFor3Digits() {
        float result = NumberUtils.round(INPUT_VALUE, 3);
        assertTrue(result == 10.556f);
    }

    /**
     * Test for {@link NumberUtils#round(float, int)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void failsForInvalidInput() {
        NumberUtils.round(INPUT_VALUE, -1);
    }

    /**
     * Test for {@link NumberUtils#round(float, int)}.
     */
    @Test
    public void roundsCorrectly() {
        float result = NumberUtils.round(INPUT_VALUE, 0);
        assertTrue(result == 11f);
    }

    /**
     * Test for {@link NumberUtils#round2Decimals(float)}.
     */
    @Test
    public void roundsCorrectly2Decimals() {
        float result = NumberUtils.round2Decimals(INPUT_VALUE);
        assertTrue(result == 10.56f);
    }
}
