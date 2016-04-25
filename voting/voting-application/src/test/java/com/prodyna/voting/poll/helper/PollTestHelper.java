package com.prodyna.voting.poll.helper;

import com.prodyna.voting.auth.helper.LoginITHelper;
import com.prodyna.voting.auth.user.UserRepository;
import com.prodyna.voting.poll.Poll;
import com.prodyna.voting.poll.PollRepository;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

public class PollTestHelper {

    private final String getAllPollsUrl;
    private final RestTemplate template;
    private final PollRepository pollRepository;
    private final LoginITHelper loginHelper;
    private String token;
    private ResponseEntity<Poll[]> allPollsResponse;
    private ResponseEntity<Poll> onePollResponse;

    public PollTestHelper(final int port, final PollRepository pollRepository,
                          final UserRepository userRepository) throws MalformedURLException {
        this.pollRepository = pollRepository;
        URL baseUrl = new URL("http://localhost:" + port + "/api/polls");
        getAllPollsUrl = baseUrl.toString();
        template = new TestRestTemplate();

        loginHelper = new LoginITHelper(port, userRepository);
    }

    public void given_a_logged_in_user_with_token() {
        loginHelper.given_some_existing_users();
        loginHelper.when_the_correct_login_credentials_are_sent();
        token = loginHelper.then_the_access_token_is_returned();
    }

    public void given_an_n_existing_number_of_polls(final int polls) {
        for (int i = 0; i < polls; i++) {
            Poll poll = new Poll();
            poll.setPollId(UUID.randomUUID().toString());
            poll.setTitle("Choose your favourite operating system: " + i);
            poll.setDescription("It cannot possibly be Windows, right?");
            poll.setChangeDate(new Date());

            pollRepository.save(poll);
        }
    }

    public void given_the_polls_with_ids(String... ids) {
        for (String id : ids) {
            Poll poll = new Poll();
            poll.setPollId(id);
            poll.setTitle("Your favourite PC game?");
            poll.setChangeDate(new Date());

            pollRepository.save(poll);
        }
    }

    public void when_get_all_polls_request_is_sent() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        allPollsResponse = template.exchange(getAllPollsUrl, HttpMethod.GET, entity, Poll[].class);
    }

    public void when_get_poll_request_with_id_is_sent(String id) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        onePollResponse = template.exchange(getAllPollsUrl + "/" + id, HttpMethod.GET, entity, Poll.class);
    }

    public void then_exactly_poll_with_id_is_returned(String id) {
        Poll pollResult = onePollResponse.getBody();
        assertTrue(pollResult != null);
        assertTrue(pollResult.getPollId().equals(id));
    }

    public void then_the_http_status_forbidden_is_returned() {
        Poll pollResult = onePollResponse.getBody();
        assertTrue(pollResult.getPollId() == null);
        assertTrue(pollResult.getTitle() == null);
        assertTrue(onePollResponse.getStatusCode().equals(HttpStatus.FORBIDDEN));
    }

    public void when_get_all_polls_with_no_authorization_request_is_sent() {
        allPollsResponse = template.getForEntity(getAllPollsUrl, Poll[].class);
    }

    public void then_exactly_n_polls_are_returned(final int n) {
        List<Poll> polls = Arrays.asList(allPollsResponse.getBody());
        assertTrue(!polls.isEmpty());
        assertTrue(polls.size() == n);
    }

    public void cleanup() {
        pollRepository.deleteAll();
        loginHelper.cleanup();
    }

    public void given_no_existing_polls() {
        // nothing happens here
    }

    public void then_no_polls_are_returned() {
        List<Poll> polls = Arrays.asList(allPollsResponse.getBody());
        assertTrue(polls.isEmpty());
    }

}
