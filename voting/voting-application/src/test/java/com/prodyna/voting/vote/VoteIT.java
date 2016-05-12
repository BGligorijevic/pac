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

import static com.prodyna.voting.poll.helper.TestPoll.ALL_POLLS;
import static com.prodyna.voting.vote.helper.TestVote.VOTE_FOR_SNICKERS;
import static com.prodyna.voting.vote.helper.TestVote.VOTE_FOR_VANILLA;

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
        $.given_a_logged_in_existing_user(VOTE_FOR_VANILLA.getUser());
        $.given_the_polls(ALL_POLLS);
        $.when_create_vote_request_is_sent(VOTE_FOR_VANILLA);
        $.then_exactly_N_votes_exist_for_user(VOTE_FOR_VANILLA.getUser().toUserObject(), 1);
    }

    @Test
    public void vote_is_denied_when_user_already_voted_on_poll() throws Exception {
        $.given_a_logged_in_existing_user(VOTE_FOR_VANILLA.getUser());
        $.given_the_polls(ALL_POLLS);
        $.when_create_vote_request_is_sent(VOTE_FOR_VANILLA);
        $.when_create_vote_request_is_sent(VOTE_FOR_SNICKERS);
        $.then_no_another_vote_for_same_poll_can_be_created();
        $.then_exactly_N_votes_exist_for_user(VOTE_FOR_VANILLA.getUser().toUserObject(), 1);
    }
}
