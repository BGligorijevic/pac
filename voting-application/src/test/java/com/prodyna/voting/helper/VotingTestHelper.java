package com.prodyna.voting.helper;

import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.prodyna.auth.helper.LoginTestHelper;
import com.prodyna.auth.user.UserRepository;
import com.prodyna.voting.Vote;
import com.prodyna.voting.VotingRepository;

public class VotingTestHelper {

    protected final String base;
    protected final RestTemplate template;
    protected final VotingRepository votingRepository;
    protected final LoginTestHelper loginHelper;
    protected String token;
    ResponseEntity<Vote[]> response;

    public VotingTestHelper(final int port, final VotingRepository votingRepository,
                            final UserRepository userRepository) throws MalformedURLException {
        this.votingRepository = votingRepository;
        URL baseUrl = new URL("http://localhost:" + port + "/api/votes");
        base = baseUrl.toString();
        template = new TestRestTemplate();

        loginHelper = new LoginTestHelper(port, userRepository);
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
