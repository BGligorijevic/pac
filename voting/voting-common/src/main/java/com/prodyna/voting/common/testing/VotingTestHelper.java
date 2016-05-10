package com.prodyna.voting.common.testing;

/**
 * Contract which all Integration test helpers should adhere to.
 */
public interface VotingTestHelper {

    /**
     * This method should clean up all the resources used in the test.
     * Example, remove all test users created during test run.
     */
    void cleanup();

    /**
     * This method should set the port used by Tomcat container run during integration tests.
     *
     * @param port Tomcat port, in Spring boot tests dynamically determined
     */
    void setTestingPort(int port);
}
