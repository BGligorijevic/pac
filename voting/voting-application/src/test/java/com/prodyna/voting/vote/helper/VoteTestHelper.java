package com.prodyna.voting.vote.helper;

import com.prodyna.voting.auth.helper.LoginITHelper;
import com.prodyna.voting.auth.helper.TestUser;
import com.prodyna.voting.auth.user.User;
import com.prodyna.voting.common.testing.VotingTestHelper;
import com.prodyna.voting.poll.helper.PollTestHelper;
import com.prodyna.voting.poll.helper.TestPoll;
import com.prodyna.voting.vote.Vote;
import com.prodyna.voting.vote.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertTrue;

@Component
public class VoteTestHelper implements VotingTestHelper {

    @Autowired
    private VoteService voteService;

    @Autowired
    private LoginITHelper loginHelper;

    @Autowired
    private PollTestHelper pollTestHelper;

    private String votesUrl;
    private final RestTemplate template = new RestTemplate();
    private String token;
    private int port;
    private boolean forbiddenReturned;

    @Override
    public void cleanup() {
        voteService.deleteAllVotes(TestUser.ADMIN_1.toUserObject());
        loginHelper.cleanup();
        pollTestHelper.cleanup();
        forbiddenReturned = false;
        token = null;
    }

    @Override
    public void setTestingPort(int port) {
        this.port = port;
        loginHelper.setTestingPort(port);
        pollTestHelper.setTestingPort(port);

        try {
            votesUrl = new URL("http://localhost:" + port + "/api/votes").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void given_a_logged_in_existing_user(TestUser loggedInTestUser) {
        token = loginHelper.given_a_logged_in_existing_user(loggedInTestUser);
    }

    public void given_the_polls(TestPoll... testPolls) {
        pollTestHelper.given_the_polls(testPolls);
    }

    public void when_create_vote_request_is_sent(TestVote testVote) {
        try {
            template.postForObject(votesUrl + "/" + testVote.getPollId() + "/" + testVote.getOptionId(), getVoteEntity(testVote), Void.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
                forbiddenReturned = true;
            }
        }
    }

    private HttpEntity<?> getVoteEntity(TestVote testVote) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);

        return new HttpEntity<>(testVote.toVoteObject(), headers);
    }

    public void then_exactly_N_votes_exist_for_user(User user, int votes) {
        List<Vote> userVotes = voteService.getUserVotes(user);
        assertTrue(userVotes.size() == votes);
    }

    public void then_no_another_vote_for_same_poll_can_be_created() {
        assertTrue(forbiddenReturned == true);
    }
}
