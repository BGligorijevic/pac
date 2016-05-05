package com.prodyna.voting.poll.helper;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.prodyna.voting.auth.helper.LoginITHelper;
import com.prodyna.voting.auth.helper.TestUser;
import com.prodyna.voting.auth.user.UserRepository;
import com.prodyna.voting.common.Nothing;
import com.prodyna.voting.poll.Poll;
import com.prodyna.voting.poll.PollRepository;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class PollTestHelper {

    private final String getAllPollsUrl;
    private final RestTemplate template;
    private final PollRepository pollRepository;
    private final LoginITHelper loginHelper;
    private String token;

    private ResponseEntity<Poll[]> allPollsResponse;
    private ResponseEntity<Poll> onePollResponse;
    private boolean forbiddenReturned;

    public PollTestHelper(int port, PollRepository pollRepository,
                          UserRepository userRepository) throws MalformedURLException {
        this.pollRepository = pollRepository;
        URL baseUrl = new URL("http://localhost:" + port + "/api/polls");
        getAllPollsUrl = baseUrl.toString();
        this.template = new RestTemplate();

        ObjectMapper om = new ObjectMapper();
        // disable auto detection
        om.disable(MapperFeature.AUTO_DETECT_CREATORS,
                MapperFeature.AUTO_DETECT_FIELDS,
                MapperFeature.AUTO_DETECT_GETTERS,
                MapperFeature.AUTO_DETECT_IS_GETTERS);
        om.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);


        loginHelper = new LoginITHelper(port, userRepository);
    }

    public void given_a_logged_in_user(TestUser loggedInTestUser) {
        loginHelper.given_existing_users(TestUser.values());
        loginHelper.when_the_correct_login_credentials_are_sent(loggedInTestUser);
        token = loginHelper.then_the_access_token_is_returned();
    }

    public void given_the_polls(TestPoll... testPolls) {
        for (TestPoll testPoll : testPolls) {
            pollRepository.save(testPoll.toPollObject());
        }
    }

    public void when_get_all_polls_request_is_sent() {
        try {
            allPollsResponse = template.exchange(getAllPollsUrl, HttpMethod.GET, getHeader(), Poll[].class);
        } catch (HttpClientErrorException e) {
            allPollsResponse = new ResponseEntity<>(e.getStatusCode());
        }
    }

    public void when_get_poll_request_with_id_is_sent(String id) {
        try {
            onePollResponse = template.exchange(getAllPollsUrl + "/" + id, HttpMethod.GET, getHeader(), Poll.class);
        } catch (HttpClientErrorException e) {
            onePollResponse = new ResponseEntity<>(e.getStatusCode());
        }
    }

    public void when_delete_poll_request_with_id_is_sent(String id) {
        try {
            template.exchange(getAllPollsUrl + "/{id}", HttpMethod.DELETE, getHeader(), String.class, id);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
                forbiddenReturned = true;
            }
        }
    }

    public void when_create_poll_request_is_sent(TestPoll testPoll) {
        try {
            template.postForObject(getAllPollsUrl, getPollEntity(testPoll), Poll.class);
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }
    }

    public void when_edit_poll_request_is_sent(TestPoll testPoll) {
        try {
            template.put(getAllPollsUrl, getPollEntity(testPoll), Poll.class);
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
        pollRepository.deleteAll();
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
