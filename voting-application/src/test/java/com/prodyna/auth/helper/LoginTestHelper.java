package com.prodyna.auth.helper;

import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.prodyna.auth.LoginResponse;
import com.prodyna.auth.user.User;
import com.prodyna.auth.user.UserRepository;

public class LoginTestHelper {

    protected final String base;
    protected final RestTemplate template;
    protected final UserRepository userRepository;
    protected ResponseEntity<LoginResponse> response;

    public LoginTestHelper(final int port, final UserRepository userRepository) throws MalformedURLException {
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

        HttpEntity<User> entity = new HttpEntity<User>(knownUser);

        response = template.postForEntity(base + "/login", entity, LoginResponse.class);
    }

    public void when_the_wrong_login_credentials_are_sent() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        User unknownUser = new User();
        unknownUser.setUserName("Tom");
        unknownUser.setPassword("i_am_trying_to_guess_pass_1_2_3");

        HttpEntity<User> entity = new HttpEntity<User>(unknownUser);

        response = template.postForEntity(base + "/login", entity, LoginResponse.class);
    }

    public String then_the_access_token_is_returned() {
        assertTrue(response != null);
        String token = response.getBody().getToken();
        assertTrue(!token.isEmpty());

        return token;
    }

    public void cleanup() {
        userRepository.deleteAll();
    }
}
