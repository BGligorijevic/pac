package com.prodyna.voting.vote.helper;

import com.prodyna.voting.auth.helper.LoginITHelper;
import com.prodyna.voting.auth.helper.TestUser;
import com.prodyna.voting.auth.user.User;
import com.prodyna.voting.common.testing.VotingTestHelper;
import com.prodyna.voting.poll.helper.PollTestHelper;
import com.prodyna.voting.poll.helper.TestPoll;
import com.prodyna.voting.vote.Vote;
import com.prodyna.voting.vote.VoteService;
import com.prodyna.voting.vote.VotingOptionResult;
import com.prodyna.voting.vote.VotingResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;

@Component
public class VoteTestHelper implements VotingTestHelper {

    @Autowired
    private VoteService voteService;

    @Autowired
    private LoginITHelper loginHelper;

    @Autowired
    private PollTestHelper pollTestHelper;

    private static final String VOTES_REST_URL = "http://localhost:8888/api/votes";
    private final RestTemplate template = new RestTemplate();
    private String token;
    private boolean forbiddenReturned;
    private ResponseEntity<VotingResults> votingResultsResponse;

    @Override
    public void cleanup() {
        voteService.deleteAllVotes(TestUser.ADMIN_1.toUserObject());
        loginHelper.cleanup();
        pollTestHelper.cleanup();
        forbiddenReturned = false;
        token = null;
        votingResultsResponse = null;
    }

    public void given_a_logged_in_existing_user(TestUser loggedInTestUser) {
        token = loginHelper.given_a_logged_in_existing_user(loggedInTestUser);
    }

    public void given_the_polls(TestPoll... testPolls) {
        pollTestHelper.given_the_polls(testPolls);
    }

    public void given_the_votes(TestVote... allVotes) {
        for (TestVote testVote : allVotes) {
            voteService.vote(testVote.getPollId(), testVote.getOptionId(), testVote.getUser().toUserObject());
        }
    }

    public void when_create_vote_request_is_sent(TestVote testVote) {
        try {
            template.postForObject(VOTES_REST_URL + "/" + testVote.getPollId() + "/" + testVote.getOptionId(), getVoteEntity(testVote), Void.class);
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

    public void then_exactly_n_votes_exist_for_user(User user, int votes) {
        List<Vote> userVotes = voteService.getUserVotes(user);
        assertTrue(userVotes.size() == votes);
    }

    public void then_no_another_vote_for_same_poll_can_be_created() {
        assertTrue(forbiddenReturned == true);
    }

    public void when_get_voting_results_request_for_poll_is_sent(TestPoll testPoll) {
        try {
            votingResultsResponse = template.exchange(VOTES_REST_URL + "/" + testPoll.get_id() + "/results", HttpMethod.GET, getHeader(), VotingResults.class);
        } catch (HttpClientErrorException e) {
            votingResultsResponse = new ResponseEntity<>(e.getStatusCode());
        }
    }

    private HttpEntity<?> getHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        return new HttpEntity<>(headers);
    }

    public void then_voting_results_for_poll_are_returned_properly(TestPoll testPoll, TestVote... votes) {
        VotingResults votingResults = votingResultsResponse.getBody();
        assertTrue(votingResults != null);
        assertTrue(votingResults.getPollId().equals(testPoll.get_id()));
        assertTrue(votingResults.getVotingOptionResults().size() == testPoll.getPollOptions().size());

        Map<String, Integer> optionsVotedWithVoteCount = getOptionsVotedWithVoteCount();

        for (VotingOptionResult votingOptionResult : votingResults.getVotingOptionResults()) {
            if (optionsVotedWithVoteCount.containsKey(votingOptionResult.getOptionId())) {
                int expected = optionsVotedWithVoteCount.get(votingOptionResult.getOptionId());
                assertTrue(expected == votingOptionResult.getCountVotes());
            }
        }
    }

    private Map<String, Integer> getOptionsVotedWithVoteCount(TestVote... votes) {
        Map<String, Integer> optionsVotedWithVoteCount = new HashMap<>();

        for (TestVote vote : votes) {
            if (!optionsVotedWithVoteCount.containsKey(vote.getOptionId())) {
                optionsVotedWithVoteCount.put(vote.getOptionId(), 1);
            } else {
                Integer count = optionsVotedWithVoteCount.get(vote.getOptionId());
                optionsVotedWithVoteCount.put(vote.getOptionId(), ++count);
            }
        }

        return optionsVotedWithVoteCount;
    }

    public void then_voting_results_for_poll_are_not_returned() {
        assertTrue(votingResultsResponse.getStatusCode().equals(HttpStatus.FORBIDDEN));
    }
}
