package com.prodyna.voting.common.testing;

/**
 * Contract which all Integration tests should adhere to.
 */
public interface VotingIntegrationTest {

    /**
     * Use this method to set up the integration test.
     *
     * @throws Exception
     */
    void setUp() throws Exception;

    /**
     * Use this method to clean up resources after integration test.
     */
    void cleanUp();
}
