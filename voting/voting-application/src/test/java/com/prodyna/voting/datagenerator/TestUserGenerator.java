package com.prodyna.voting.datagenerator;

import com.prodyna.voting.Application;
import com.prodyna.voting.auth.helper.TestUser;
import com.prodyna.voting.auth.user.UserService;
import com.prodyna.voting.poll.Poll;
import com.prodyna.voting.poll.PollOption;
import com.prodyna.voting.poll.PollService;
import com.prodyna.voting.poll.helper.TestPoll;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Meant to be used when testing UI manually.
 * Let these tests run with build, its safe, since the data is immediately deleted anyway.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class TestUserGenerator {

    @Autowired
    private UserService userService;

    @Autowired
    private PollService pollService;

    @Test
    public void generateTestData() {
        for (TestUser user : TestUser.ALL_USERS) {
            userService.saveUser(user.toUserObject());
        }

        for (TestPoll poll : TestPoll.ALL_POLLS) {
            pollService.createPoll(poll.toPollObject());
        }
    }

    @Test
    public void removeAllTestData() {
        userService.deleteAllUsers(TestUser.ADMIN_1.toUserObject());
        pollService.deleteAllPolls(TestUser.ADMIN_1.toUserObject());
    }

    @Test
    public void removeVotes() {
        List<Poll> allPolls = pollService.getAllPolls();
        for (Poll poll : allPolls) {
            for (PollOption pollOption : poll.getPollOptions()) {
                pollOption.setVotes(new ArrayList<>());
            }
            pollService.editPoll(poll, TestUser.ADMIN_1.toUserObject());
        }
    }
}
