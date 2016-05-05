package com.prodyna.voting.vote;

import com.prodyna.voting.Application;
import com.prodyna.voting.auth.user.UserRepository;
import com.prodyna.voting.poll.PollRepository;
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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
public class VoteIT {

    @Value("${local.server.port}")
    private int tomcatPort;
    private VoteTestHelper $;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PollRepository pollRepository;

    @Before
    public void setUp() throws Exception {
        $ = new VoteTestHelper();
    }

    @After
    public void tearDown() {
        $.cleanup();
    }

    @Test
    public void vote_is_created() throws Exception {

    }
}
