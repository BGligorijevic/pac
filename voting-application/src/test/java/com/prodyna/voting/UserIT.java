package com.prodyna.voting;

import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import com.prodyna.Application;
import com.prodyna.auth.LoginResponse;
import com.prodyna.auth.user.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({ "server.port=0" })
public class UserIT {

    @Value("${local.server.port}")
    private int port;

    private String base;
    private RestTemplate template;

    @Before
    public void setUp() throws Exception {
	URL baseUrl = new URL("http://localhost:" + port + "/user");
	base = baseUrl.toString();
	template = new TestRestTemplate();
    }

    @Test
    public void login_succeeds() throws Exception {
	User user = new User();
	user.setUserName("Tom");
	user.setPassword("Jones");

	HttpHeaders headers = new HttpHeaders();
	headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	HttpEntity<User> entity = new HttpEntity<User>(user);

	ResponseEntity<LoginResponse> response = template.postForEntity(base + "/login", entity, LoginResponse.class);
	assertTrue(response != null);
	assertTrue(!response.getBody().getToken().isEmpty());
    }

    @Test
    public void login_fails() throws Exception {
	User user = new User();
	user.setUserName("Mike");
	user.setPassword("Douglas");

	HttpHeaders headers = new HttpHeaders();
	headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	HttpEntity<User> entity = new HttpEntity<User>(user);

	ResponseEntity<LoginResponse> response = template.postForEntity(base + "/login", entity, LoginResponse.class);
	assertTrue(response != null);
	assertTrue(response.getStatusCode() == HttpStatus.UNAUTHORIZED);
    }
}
