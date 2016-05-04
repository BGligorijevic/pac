package com.prodyna.voting.common;

import java.util.Collection;
import java.util.Optional;

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

    public static <T> void ifAbsent(Optional<T> optional, String description) {
        if (!optional.isPresent()) {
            always(description);
        }
    }

    public static void ifLessElementsThan(Collection collection, int n, String description) {
        ifNull(collection, description);

        if (collection.size() < n) {
            always(description);
        }
    }
}
