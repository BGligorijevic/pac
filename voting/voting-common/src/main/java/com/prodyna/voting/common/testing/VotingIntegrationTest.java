package com.prodyna.voting.common.testing;

/**
 * Contract which all Integration tests should adhere to.
 */
public interface VotingIntegrationTest {

    /**
     * Use this method to clean up resources before integration test.
     */
    void cleanUpBefore();

    /**
     * Use this method to clean up resources after integration test.
     */
    void cleanUpAfter();
}
