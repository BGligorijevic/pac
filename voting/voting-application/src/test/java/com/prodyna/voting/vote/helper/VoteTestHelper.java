package com.prodyna.voting.vote.helper;

import com.prodyna.voting.auth.helper.TestUser;
import com.prodyna.voting.auth.user.UserService;
import com.prodyna.voting.common.testing.VotingTestHelper;
import com.prodyna.voting.poll.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VoteTestHelper implements VotingTestHelper {

    @Autowired
    private UserService userService;

    @Autowired
    private PollService pollService;

    private int port;

    @Override
    public void cleanup() {
        userService.deleteAllUsers(TestUser.ADMIN_1.toUserObject());
        pollService.deleteAllPolls(TestUser.ADMIN_1.toUserObject());
    }

    @Override
    public void setTestingPort(int port) {
        this.port = port;
    }
}
