package com.prodyna.voting.poll.helper;

import com.prodyna.voting.auth.helper.LoginITHelper;
import com.prodyna.voting.auth.helper.TestUser;
import com.prodyna.voting.common.Nothing;
import com.prodyna.voting.common.testing.VotingTestHelper;
import com.prodyna.voting.poll.Poll;
import com.prodyna.voting.poll.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

@Component
public class PollTestHelper implements VotingTestHelper {

    @Autowired
    private PollService pollService;

    @Autowired
    private LoginITHelper loginHelper;

    private static final String POLLS_REST_URL = "http://localhost:8888/api/polls";
    private final RestTemplate template = new RestTemplate();
    private String token;
    private ResponseEntity<Poll[]> allPollsResponse;
    private ResponseEntity<Poll> onePollResponse;
    private boolean forbiddenReturned;

    public void given_a_logged_in_existing_user(TestUser loggedInTestUser) {
        token = loginHelper.given_a_logged_in_existing_user(loggedInTestUser);
    }

    public void given_the_polls(TestPoll... testPolls) {
        for (TestPoll testPoll : testPolls) {
            pollService.createPoll(testPoll.toPollObject());
        }
    }

    public void when_get_all_polls_request_is_sent() {
        try {
            allPollsResponse = template.exchange(POLLS_REST_URL, HttpMethod.GET, getHeader(), Poll[].class);
        } catch (HttpClientErrorException e) {
            allPollsResponse = new ResponseEntity<>(e.getStatusCode());
        }
    }

    public void when_get_poll_request_with_id_is_sent(String id) {
        try {
            onePollResponse = template.exchange(POLLS_REST_URL + "/" + id, HttpMethod.GET, getHeader(), Poll.class);
        } catch (HttpClientErrorException e) {
            onePollResponse = new ResponseEntity<>(e.getStatusCode());
        }
    }

    public void when_delete_poll_request_with_id_is_sent(String id) {
        try {
            template.exchange(POLLS_REST_URL + "/{id}", HttpMethod.DELETE, getHeader(), String.class, id);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
                forbiddenReturned = true;
            }
        }
    }

    public void when_create_poll_request_is_sent(TestPoll testPoll) {
        try {
            template.postForObject(POLLS_REST_URL, getPollEntity(testPoll), Poll.class);
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }
    }

    public void when_edit_poll_request_is_sent(TestPoll testPoll) {
        try {
            template.put(POLLS_REST_URL, getPollEntity(testPoll), Poll.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
                forbiddenReturned = true;
            }
        }
    }

    public void then_poll_has_correct_data(TestPoll testPoll) {
        Poll pollResult = onePollResponse.getBody();
        assertTrue(pollResult != null);

        assertTrue(testPoll.getTitle().equals(pollResult.getTitle()));
        assertTrue(testPoll.getPollOptions().size() == pollResult.getPollOptions().size());
    }

    public void then_no_poll_is_deleted() {
        assertTrue(forbiddenReturned == true);
    }

    public void then_no_poll_is_edited() {
        assertTrue(forbiddenReturned == true);
    }

    public void then_N_polls_exist(int n) {
        when_get_all_polls_request_is_sent();
        then_exactly_N_polls_are_returned(n);
    }

    public void then_exactly_poll_with_id_is_returned(String id) {
        Poll pollResult = onePollResponse.getBody();
        assertTrue(pollResult != null);
        assertTrue(pollResult.get_id().equals(id));
    }

    public void then_the_http_status_unauthorized_is_returned() {
        if (onePollResponse != null) {
            assertTrue(onePollResponse.getStatusCode().equals(HttpStatus.UNAUTHORIZED));
        } else if (allPollsResponse != null) {
            assertTrue(allPollsResponse.getStatusCode().equals(HttpStatus.UNAUTHORIZED));
        }
    }

    public void then_exactly_N_polls_are_returned(final int n) {
        List<Poll> polls = Arrays.asList(allPollsResponse.getBody());
        assertTrue(!polls.isEmpty());
        assertTrue(polls.size() == n);
    }

    public void cleanup() {
        pollService.deleteAllPolls(TestUser.ADMIN_1.toUserObject());
        loginHelper.cleanup();
        forbiddenReturned = false;
        onePollResponse = null;
        allPollsResponse = null;
        token = null;
    }

    public void given_no_existing_polls() {
        Nothing.onPurpose();
    }

    public void then_no_polls_are_returned() {
        List<Poll> polls = Arrays.asList(allPollsResponse.getBody());
        assertTrue(polls.isEmpty());
    }

    private HttpEntity<?> getHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        return new HttpEntity<>(headers);
    }

    private HttpEntity<?> getPollEntity(TestPoll testPoll) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);

        return new HttpEntity<>(testPoll.toPollObject(), headers);
    }
}
