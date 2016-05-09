package com.prodyna.voting.common.testing;

/**
 * Contract which all Integration tests should adhere to.
 */
public interface VotingIntegrationTest {

    void setUp() throws Exception;

    void tearDown();
}
