package com.prodyna.voting.vote.helper;

import com.prodyna.voting.auth.helper.LoginITHelper;
import com.prodyna.voting.auth.user.UserRepository;
import com.prodyna.voting.vote.Vote;
import com.prodyna.voting.vote.VotingRepository;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class VotingTestHelper {

    protected final String base;
    protected final RestTemplate template;
    protected final VotingRepository votingRepository;
    protected final LoginITHelper loginHelper;
    protected String token;
    ResponseEntity<Vote[]> response;

    public VotingTestHelper(final int port, final VotingRepository votingRepository,
                            final UserRepository userRepository) throws MalformedURLException {
        this.votingRepository = votingRepository;
        URL baseUrl = new URL("http://localhost:" + port + "/api/votes");
        base = baseUrl.toString();
        template = new TestRestTemplate();

        loginHelper = new LoginITHelper(port, userRepository);
    }

    public void given_a_logged_in_user_with_token() {
        loginHelper.given_some_existing_users();
        loginHelper.when_the_correct_login_credentials_are_sent();
        token = loginHelper.then_the_access_token_is_returned();
    }

    public void given_an_n_existing_number_of_votes(final int votes) {
        for (int i = 0; i < votes; i++) {
            Vote vote = new Vote();
            vote.setTitle("Choose your favourite operating system - voting: " + i);
            vote.setDescription("It cannot possibly be Windows, right?");
            vote.setCreated(new Date());

            votingRepository.save(vote);
        }
    }

    public void when_get_all_votes_request_is_sent() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        response = template.exchange(base, HttpMethod.GET, entity, Vote[].class);
    }

    public void when_get_all_votes_with_no_authorization_request_is_sent() {
        response = template.getForEntity(base, Vote[].class);
    }

    public void then_exactly_n_votes_are_returned(final int n) {
        List<Vote> votes = Arrays.asList(response.getBody());
        assertTrue(!votes.isEmpty());
        assertTrue(votes.size() == n);
    }

    public void cleanup() {
        votingRepository.deleteAll();
        loginHelper.cleanup();
    }

    public void given_no_existing_votes() {
        // nothing happens here
    }

    public void then_no_votes_are_returned() {
        List<Vote> votes = Arrays.asList(response.getBody());
        assertTrue(votes.isEmpty());
    }
}
