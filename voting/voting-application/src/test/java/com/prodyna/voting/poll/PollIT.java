package com.prodyna.voting.poll;

import com.prodyna.voting.Application;
import com.prodyna.voting.auth.user.UserService;
import com.prodyna.voting.poll.helper.PollTestHelper;
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

import static com.prodyna.voting.auth.helper.TestUser.*;
import static com.prodyna.voting.poll.helper.TestPoll.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
public class PollIT {

    @Value("${local.server.port}")
    private int tomcatPort;
    private PollTestHelper $;

    @Autowired
    private UserService userService;

    @Autowired
    private PollService pollService;

    @Before
    public void setUp() throws Exception {
        $ = new PollTestHelper(tomcatPort, pollService, userService);
    }

    @After
    public void tearDown() {
        $.cleanup();
    }

    @Test
    public void all_polls_are_returned() throws Exception {
        $.given_a_logged_in_user(USER_1);
        $.given_the_polls(CAR, ICE_CREAM);
        $.when_get_all_polls_request_is_sent();
        $.then_exactly_N_polls_are_returned(2);
    }

    @Test
    public void get_all_polls_not_returned_when_user_is_not_authenticated() throws Exception {
        $.given_the_polls(CAR, ICE_CREAM);
        $.when_get_all_polls_request_is_sent();
        $.then_the_http_status_unauthorized_is_returned();
    }

    @Test
    public void no_polls_are_returned_when_no_polls_exist() throws Exception {
        $.given_a_logged_in_user(USER_1);
        $.given_no_existing_polls();
        $.when_get_all_polls_request_is_sent();
        $.then_no_polls_are_returned();
    }

    @Test
    public void correct_poll_is_returned() {
        $.given_a_logged_in_user(USER_1);
        $.given_the_polls(ALL_POLLS);
        $.when_get_poll_request_with_id_is_sent(CAR.get_id());
        $.then_exactly_poll_with_id_is_returned(CAR.get_id());
    }

    @Test
    public void get_poll_forbidden_when_user_is_not_authenticated() {
        $.given_the_polls(ALL_POLLS);
        $.when_get_poll_request_with_id_is_sent(CAR.get_id());
        $.then_the_http_status_unauthorized_is_returned();
    }

    @Test
    public void delete_poll_succeeds_if_admin() {
        $.given_a_logged_in_user(ADMIN_1);
        $.given_the_polls(ALL_POLLS);
        $.when_delete_poll_request_with_id_is_sent(ICE_CREAM.get_id());
        $.then_N_polls_exist(ALL_POLLS.length - 1);
    }

    @Test
    public void delete_poll_fails_if_not_admin_and_not_his_own_poll() {
        $.given_a_logged_in_user(USER_1);
        $.given_the_polls(ALL_POLLS);
        $.when_delete_poll_request_with_id_is_sent(CAR.get_id());
        $.then_no_poll_is_deleted();
    }

    @Test
    public void delete_poll_succeeds_if_his_own_poll() {
        $.given_a_logged_in_user(USER_1);
        $.given_the_polls(ALL_POLLS);
        $.when_delete_poll_request_with_id_is_sent(OS.get_id());
        $.then_N_polls_exist(ALL_POLLS.length - 1);
    }

    @Test
    public void create_poll_succeeds() {
        $.given_a_logged_in_user(USER_2);
        $.given_the_polls(OS);
        $.when_create_poll_request_is_sent(ICE_CREAM);
        $.then_N_polls_exist(2);
    }

    @Test
    public void edit_poll_succeeds() {
        $.given_a_logged_in_user(USER_1);
        $.given_the_polls(ALL_POLLS);
        $.when_edit_poll_request_is_sent(CHANGED_ICE_CREAM);
        $.then_N_polls_exist(ALL_POLLS.length);

        $.when_get_poll_request_with_id_is_sent(ICE_CREAM.get_id());
        $.then_poll_has_correct_data(CHANGED_ICE_CREAM);
    }

    @Test
    public void edit_poll_fails_if_not_admin_and_not_his_own_poll() {
        $.given_a_logged_in_user(USER_2);
        $.given_the_polls(ALL_POLLS);
        $.when_edit_poll_request_is_sent(CHANGED_ICE_CREAM);
        $.then_no_poll_is_edited();
    }
}
