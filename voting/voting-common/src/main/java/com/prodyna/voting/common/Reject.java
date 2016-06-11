package com.prodyna.voting.common;

import lombok.extern.log4j.Log4j;

import java.util.Collection;
import java.util.Optional;

/**
 * Utility function to make it easier to work with null checks.
 */
@Log4j
public final class Reject {

    /**
     * You shall not pass!
     */
    private Reject() {
    }

    /**
     * Unconditionally throws a {@link IllegalArgumentException}.
     * Wrapper method for such exception.
     *
     * @param description Text to add to exception
     */
    public static void always(String description) {
        log.error(description);
        throw new IllegalArgumentException(description);
    }

    /**
     * Throws a {@link IllegalArgumentException} if the expression to test is false.
     * Wrapper method for such exception.
     *
     * @param expressionToTest Flag to examine
     * @param description      Text to add to exception
     */
    public static void ifFalse(boolean expressionToTest, String description) {
        if (!expressionToTest) {
            always(description);
        }
    }

    /**
     * Throws an {@link IllegalArgumentException} if passed input is null.
     *
     * @param object      Object to test if null
     * @param description Text to add to exception
     */
    public static void ifNull(Object object, String description) {
        if (object == null) {
            always(description);
        }
    }

    /**
     * Throws an {@link IllegalArgumentException} if passed Optional is empty.
     *
     * @param optional    Optional object to test if null
     * @param description Text to add to exception
     */
    public static <T> void ifAbsent(Optional<T> optional, String description) {
        if (!optional.isPresent()) {
            always(description);
        }
    }

    /**
     * Throws an {@link IllegalArgumentException} if passed collection is null or has number of elements less than specified.
     *
     * @param collection       Collection to test
     * @param numberOfElements Number of elements to require in the collection
     * @param description      Text to add to exception
     */
    public static void ifLessElementsThan(Collection collection, int numberOfElements, String description) {
        ifNull(collection, description);

        if (collection.size() < numberOfElements) {
            always(description);
        }
    }
}
