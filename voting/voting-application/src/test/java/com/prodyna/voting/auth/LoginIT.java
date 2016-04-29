package com.prodyna.voting.auth;

import com.prodyna.voting.auth.helper.LoginITHelper;
import com.prodyna.voting.Application;
import com.prodyna.voting.auth.user.UserRepository;
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

import java.net.MalformedURLException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
public class LoginIT {

    @Autowired
    private UserRepository userRepository;

    @Value("${local.server.port}")
    private int port;
    private LoginITHelper $;

    @Before
    public void setUp() throws MalformedURLException {
        $ = new LoginITHelper(port, userRepository);
    }

    @Test
    public void login_succeeds_for_existing_user() throws Exception {
        $.given_some_existing_users();
        $.when_the_correct_login_credentials_are_sent("Tom", "tom_jones_446");
        $.then_the_access_token_is_returned();
    }

    @Test
    public void login_fails_for_unknown_user() {
        $.given_some_existing_users();
        $.when_the_wrong_login_credentials_are_sent();
        $.then_the_unauthorized_status_code_is_returned();
    }

    @After
    public void tearDown() {
        $.cleanup();
    }
}
