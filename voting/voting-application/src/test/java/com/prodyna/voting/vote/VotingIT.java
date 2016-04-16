package com.prodyna.voting.vote;

import com.prodyna.voting.Application;
import com.prodyna.voting.auth.user.UserRepository;
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
import com.prodyna.voting.vote.helper.VotingTestHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
public class VotingIT  {

    @Value("${local.server.port}")
    private int tomcatPort;
    private VotingTestHelper $;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VotingRepository votingRepository;

    @Before
    public void setUp() throws Exception {
        $ = new VotingTestHelper(tomcatPort, votingRepository, userRepository);
    }

    @Test
    public void gets_all_votes_returns_votes() throws Exception {
        $.given_a_logged_in_user_with_token();
        $.given_an_n_existing_number_of_votes(2);
        $.when_get_all_votes_request_is_sent();
        $.then_exactly_n_votes_are_returned(2);
    }

    @Test
    public void get_all_votes_returns_zero_results() throws Exception {
        $.given_a_logged_in_user_with_token();
        $.given_no_existing_votes();
        $.when_get_all_votes_request_is_sent();
        $.then_no_votes_are_returned();
    }

    @After
    public void tearDown() {
        $.cleanup();
    }
}
