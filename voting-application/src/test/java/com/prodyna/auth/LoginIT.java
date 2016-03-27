package com.prodyna.auth;

import java.net.MalformedURLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.prodyna.Application;
import com.prodyna.VotingApplicationIntegrationTest;
import com.prodyna.auth.helper.LoginTestHelper;
import com.prodyna.auth.user.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({ "server.port=0" })
public class LoginIT implements VotingApplicationIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Value("${local.server.port}")
    private int port;
    private LoginTestHelper $;

    @Override
    @Before
    public void setUp() throws MalformedURLException {
	$ = new LoginTestHelper(port, userRepository);
    }

    @Test
    public void login_succeeds_for_existing_user() throws Exception {
	$.given_some_existing_users();
	$.when_the_correct_login_credentials_are_sent();
	$.then_the_access_token_is_returned();
    }

    @Test
    public void login_fails_for_non_existing_user() {
	$.given_some_existing_users();
	$.when_the_wrong_login_credentials_are_sent();
	$.then_the_unauthorized_status_code_is_returned();
    }

    @Override
    @After
    public void tearDown() {
	$.cleanup();
    }
}
