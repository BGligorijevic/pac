package com.prodyna.voting.common;

/**
 * Utility function to make it easier to work with null checks.
 */
public final class Reject {

    private Reject() {
    }

    public static void always(String description) {
        throw new IllegalArgumentException(description);
    }

    public static void ifNull(Object object, String description) {
        if (object == null) {
            always(description);
        }
    }
}
