package com.prodyna.voting.common;

import java.math.BigDecimal;

/**
 * Utility function to make it easier to work with numbers.
 */
public class NumberUtils {

    /**
     * You shall not pass!
     */
    private NumberUtils() {
    }

    /**
     * Rounds to a certain number of decimal places.
     *
     * @param value        Value to round
     * @param decimalPlace Places to round
     * @return Rounded float value
     * @throws IllegalArgumentException If decimal places are less than 0
     */
    public static float round(float value, int decimalPlace) {
        if (decimalPlace < 0) {
            Reject.always("Decimal places cannot be less than 0.");
        }

        BigDecimal bd = new BigDecimal(Float.toString(value));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);

        return bd.floatValue();
    }

    /**
     * Round to 2 decimal places.
     *
     * @param value Value to round
     * @return Rounded float value
     */
    public static float round2Decimals(float value) {
        return round(value, 2);
    }
}
