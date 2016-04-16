package com.prodyna.voting.auth.helper;

import com.prodyna.voting.auth.user.User;
import com.prodyna.voting.auth.user.UserRepository;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;

public class LoginITHelper {

    private final String base;
    private final RestTemplate template;
    private final UserRepository userRepository;
    private ResponseEntity<String> response;

    public LoginITHelper(final int port, final UserRepository userRepository) throws MalformedURLException {
        this.userRepository = userRepository;
        URL baseUrl = new URL("http://localhost:" + port + "/user");
        base = baseUrl.toString();
        template = new TestRestTemplate();
    }

    public void given_some_existing_users() {
        User user1 = new User();
        user1.setUserName("Tom");
        user1.setPassword("tom_jones_446");
        userRepository.save(user1);

        User user2 = new User();
        user2.setUserName("Dirk");
        user2.setPassword("Nowitzki");
        userRepository.save(user2);
    }

    public void then_the_unauthorized_status_code_is_returned() {
        assertTrue(response != null);
        assertTrue(response.getStatusCode() == HttpStatus.UNAUTHORIZED);
    }

    public void when_the_correct_login_credentials_are_sent() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        User knownUser = new User();
        knownUser.setUserName("Tom");
        knownUser.setPassword("tom_jones_446");

        HttpEntity<User> entity = new HttpEntity<>(knownUser);

        response = template.postForEntity(base + "/login", entity, String.class);
    }

    public void when_the_wrong_login_credentials_are_sent() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        User unknownUser = new User();
        unknownUser.setUserName("Tom");
        unknownUser.setPassword("i_am_trying_to_guess_pass_1_2_3");

        HttpEntity<User> entity = new HttpEntity<>(unknownUser);

        response = template.postForEntity(base + "/login", entity, String.class);
    }

    public String then_the_access_token_is_returned() {
        assertTrue(response != null);
        String token = response.getBody();
        assertTrue(!token.isEmpty());

        return token;
    }

    public void cleanup() {
        userRepository.deleteAll();
    }
}
