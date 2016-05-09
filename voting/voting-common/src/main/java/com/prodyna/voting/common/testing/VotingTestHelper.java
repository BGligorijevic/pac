package com.prodyna.voting.common.testing;

/**
 * Contract which all Integration test helpers should adhere to.
 */
public interface VotingTestHelper {

    void cleanup();

    void setTestingPort(int port);
}
