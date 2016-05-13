package com.prodyna.voting.auth.helper;

import com.prodyna.voting.auth.user.User;
import com.prodyna.voting.auth.user.UserService;
import com.prodyna.voting.common.testing.VotingTestHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.junit.Assert.assertTrue;

@Component
public class LoginITHelper implements VotingTestHelper {

    private static final String LOGIN_REST_URL = "http://localhost:8888/user/login";
    private RestTemplate template = new TestRestTemplate();

    @Autowired
    private UserService userService;
    private ResponseEntity<String> response;

    public void given_existing_users(TestUser... testUsers) {
        for (TestUser testUser : testUsers) {
            userService.saveUser(testUser.toUserObject());
        }
    }

    public void when_the_correct_login_credentials_are_sent(TestUser user) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        User knownUser = new User();
        knownUser.setUserName(user.getUsername());
        knownUser.setPassword(user.getPass());

        HttpEntity<User> entity = new HttpEntity<>(knownUser);

        response = template.postForEntity(LOGIN_REST_URL, entity, String.class);
    }

    public void when_the_wrong_login_credentials_are_sent() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        User unknownUser = new User();
        unknownUser.setUserName(TestUser.USER_1.getUsername());
        unknownUser.setPassword("some_wrong_pass_1_2_3");

        HttpEntity<User> entity = new HttpEntity<>(unknownUser);

        response = template.postForEntity(LOGIN_REST_URL, entity, String.class);
    }

    public String then_the_access_token_is_returned() {
        assertTrue(response != null);
        String token = response.getBody();
        assertTrue(!token.isEmpty());

        return token;
    }

    public void then_the_unauthorized_status_code_is_returned() {
        assertTrue(response != null);
        assertTrue(response.getStatusCode() == HttpStatus.UNAUTHORIZED);
    }

    public void cleanup() {
        userService.deleteAllUsers(TestUser.ADMIN_1.toUserObject());
    }

    public String given_a_logged_in_existing_user(TestUser loggedInTestUser) {
        given_existing_users(TestUser.values());
        when_the_correct_login_credentials_are_sent(loggedInTestUser);

        return then_the_access_token_is_returned();
    }
}
