package com.prodyna.voting.common;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@RunWith(PowerMockRunner.class)
public class RejectTest {

    /**
     * Test for {@link Reject#always(String)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void rejectsAlways() {
        Reject.always("Some message...");
    }

    /**
     * Test for {@link Reject#always(String)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void rejectsAlwaysEvenWithoutMessage() {
        Reject.always(null);
    }

    /**
     * Test for {@link Reject#ifNull(Object, String)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void rejectsIfNull() {
        Reject.ifNull(null, "Null is not a valid input.");
    }

    /**
     * Test for {@link Reject#ifNull(Object, String)}.
     */
    @Test
    public void doesNotRejectIfNotNull() {
        Reject.ifNull(new Date(), "That is a valid input.");
    }

    /**
     * Test for {@link Reject#ifLessElementsThan(Collection, int, String)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void rejectsIfLessElementsThan() {
        List list = new ArrayList<>();
        list.add("Bla");
        list.add("Blablabla");

        Reject.ifLessElementsThan(list, 3, "List must have at least 3 elements!");
    }

    /**
     * Test for {@link Reject#ifLessElementsThan(Collection, int, String)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void rejectsIfLessElementsIsCalledWithNullInput() {
        Reject.ifLessElementsThan(null, 3, "List must have at least 3 elements!");
    }

    /**
     * Test for {@link Reject#ifLessElementsThan(Collection, int, String)}.
     */
    @Test
    public void doesNotRejectIfLessElementsDoesNotApply() {
        List list = new ArrayList<>();
        list.add("Bla");
        list.add("Blablabla");
        Reject.ifLessElementsThan(list, 2, "List must have at least 2 elements!");
    }
}
