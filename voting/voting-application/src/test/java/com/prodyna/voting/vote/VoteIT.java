package com.prodyna.voting.vote;

import com.prodyna.voting.Application;
import com.prodyna.voting.common.testing.VotingIntegrationTest;
import com.prodyna.voting.vote.helper.VoteTestHelper;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static com.prodyna.voting.auth.helper.TestUser.*;
import static com.prodyna.voting.poll.helper.TestPoll.ALL_POLLS;
import static com.prodyna.voting.poll.helper.TestPoll.ICE_CREAM;
import static com.prodyna.voting.vote.helper.TestVote.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port=8888"})
public class VoteIT implements VotingIntegrationTest {

    @Autowired
    private VoteTestHelper $;

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
        $.then_exactly_n_votes_exist_for_user(VOTE_FOR_VANILLA.getUser().toUserObject(), 1);
    }

    @Test
    public void vote_is_denied_when_user_already_voted_on_poll() throws Exception {
        $.given_a_logged_in_existing_user(VOTE_FOR_VANILLA.getUser());
        $.given_the_polls(ALL_POLLS);
        $.when_create_vote_request_is_sent(VOTE_FOR_VANILLA);
        $.when_create_vote_request_is_sent(VOTE_FOR_SNICKERS);
        $.then_no_another_vote_for_same_poll_can_be_created();
        $.then_exactly_n_votes_exist_for_user(VOTE_FOR_VANILLA.getUser().toUserObject(), 1);
    }

    @Test
    public void voting_results_are_returned_properly() {
        $.given_a_logged_in_existing_user(USER_1);
        $.given_the_polls(ALL_POLLS);
        $.given_the_votes(ALL_VOTES);
        $.when_get_voting_results_request_for_poll_is_sent(ICE_CREAM);
        $.then_voting_results_for_poll_are_returned_properly(ICE_CREAM, ALL_VOTES);
    }

    @Test
    public void voting_results_are_returned_always_if_user_is_admin() {
        $.given_a_logged_in_existing_user(ADMIN_1);
        $.given_the_polls(ALL_POLLS);
        $.given_the_votes(VOTE_FOR_VANILLA);
        $.when_get_voting_results_request_for_poll_is_sent(ICE_CREAM);
        $.then_voting_results_for_poll_are_returned_properly(ICE_CREAM, VOTE_FOR_VANILLA);
    }

    @Test
    public void voting_results_are_not_returned_if_user_has_not_voted_on_poll() {
        $.given_a_logged_in_existing_user(USER_2);
        $.given_the_polls(ALL_POLLS);
        $.given_the_votes(VOTE_FOR_VANILLA);
        $.when_get_voting_results_request_for_poll_is_sent(ICE_CREAM);
        $.then_voting_results_for_poll_are_not_returned();
    }
}
