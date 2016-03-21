package com.prodyna.voting;

import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import com.prodyna.Application;
import com.prodyna.auth.LoginResponse;
import com.prodyna.auth.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({ "server.port=0" })
public class VotingIT {

	@Value("${local.server.port}")
	private int port;

	private String base;
	private RestTemplate template;

	@Before
	public void setUp() throws Exception {
		URL baseUrl = new URL("http://localhost:" + port + "/api/votes");
		base = baseUrl.toString();
		template = new TestRestTemplate();
	}

	private String login() {
		User user = new User();
		user.setUserName("tom");

		HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    HttpEntity<User> entity = new HttpEntity<User>(user);

		ResponseEntity<LoginResponse> response = template.postForEntity("http://localhost:" + port + "/user/login",
				entity, LoginResponse.class);
		assertTrue(response != null);

		return response.getBody().getToken();
	}

	@Test
	public void getAllVotes() throws Exception {
		String token = login();

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + token);

		HttpEntity<?> entity = new HttpEntity<>(headers);

		ResponseEntity<Vote[]> response = template.exchange(
				base, HttpMethod.GET, entity, Vote[].class);
		List<Vote> votes = Arrays.asList(response.getBody());
		assertTrue(!votes.isEmpty());
		assertTrue(votes.size() == 1);
	}

	@Test
	public void getAllVotesFailsWithoutToken() throws Exception {
		ResponseEntity<String> response = template.getForEntity(base, String.class);
		assertTrue(response.getStatusCode() == HttpStatus.FORBIDDEN);
	}

	@Test
	@Ignore
	public void getOneVote() throws Exception {
		ResponseEntity<Vote[]> response = template.getForEntity(base + "/1", Vote[].class);
		assertTrue(response.getStatusCode() == HttpStatus.FORBIDDEN);
	}
}
