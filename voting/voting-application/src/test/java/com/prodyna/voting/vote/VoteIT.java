package com.prodyna.voting.vote;

import com.prodyna.voting.Application;
import com.prodyna.voting.common.testing.VotingIntegrationTest;
import com.prodyna.voting.vote.helper.VoteTestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
public class VoteIT implements VotingIntegrationTest {

    @Autowired
    private VoteTestHelper $;

    @Value("${local.server.port}")
    private int tomcatPort;

    @Before
    @Override
    public void setUp() throws Exception {
        $.setTestingPort(tomcatPort);
    }

    @After
    @Override
    public void cleanUp() {
        $.cleanup();
    }

    @Test
    public void vote_is_created() throws Exception {
        assertTrue($ != null);
    }
}
