package com.prodyna.voting.auth;

import com.prodyna.voting.Application;
import com.prodyna.voting.auth.helper.LoginITHelper;
import com.prodyna.voting.common.testing.VotingIntegrationTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static com.prodyna.voting.datagenerator.sampledata.TestUser.ALL_USERS;
import static com.prodyna.voting.datagenerator.sampledata.TestUser.USER_1;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port=8888"})
public class LoginIT implements VotingIntegrationTest {

    @Autowired
    private LoginITHelper $;

    @Test
    public void login_succeeds_for_existing_user() throws Exception {
        $.given_existing_users(ALL_USERS);
        $.when_the_correct_login_credentials_are_sent(USER_1);
        $.then_the_access_token_is_returned();
    }

    @Test
    public void login_fails_for_unknown_user() {
        $.given_existing_users(ALL_USERS);
        $.when_the_wrong_login_credentials_are_sent();
        $.then_the_unauthorized_status_code_is_returned();
    }

    @Before
    @Override
    public void cleanUpBefore() {
        $.cleanup();
    }

    @After
    @Override
    public void cleanUpAfter() {
        $.cleanup();
    }
}
