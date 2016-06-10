package com.prodyna.voting.datagenerator;

import com.prodyna.voting.Application;
import com.prodyna.voting.auth.helper.TestUser;
import com.prodyna.voting.auth.user.UserService;
import com.prodyna.voting.poll.PollService;
import com.prodyna.voting.poll.helper.TestPoll;
import com.prodyna.voting.vote.VoteService;
import com.prodyna.voting.vote.helper.TestVote;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Meant to be used when testing UI manually.
 * All tests will be deliberately set on ignore.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class TestUserGenerator {

    @Autowired
    private UserService userService;

    @Autowired
    private PollService pollService;

    @Autowired
    private VoteService voteService;

    @Test
    @Ignore
    public void generateTestData() {
        for (TestUser user : TestUser.ALL_USERS) {
            userService.saveUser(user.toUserObject());
        }

        for (TestPoll poll : TestPoll.ALL_POLLS) {
            pollService.createPoll(poll.toPollObject());
        }

        for (TestVote vote : TestVote.ALL_VOTES) {
            voteService.vote(vote.getPollId(), vote.getOptionId(), vote.getUser().toUserObject());
        }
    }
}
