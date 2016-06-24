package com.prodyna.voting.sampledata;

import com.prodyna.voting.auth.user.UserService;
import com.prodyna.voting.poll.Poll;
import com.prodyna.voting.poll.PollOption;
import com.prodyna.voting.poll.PollService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataGenerator {

    @Value("${voting.dev.mode}")
    @Getter
    private String testMode;

    @Autowired
    private UserService userService;

    @Autowired
    private PollService pollService;

    @PostConstruct
    public void postInit() {
        if (testMode != null && testMode.equalsIgnoreCase("true")) {
            regenerateTestData();
        }
    }

    public void regenerateTestData() {
        removeAllTestData();

        for (TestUser user : TestUser.ALL_USERS) {
            userService.saveUser(user.toUserObject());
        }

        for (TestPoll poll : TestPoll.ALL_POLLS) {
            pollService.createPoll(poll.toPollObject(), null);
        }
    }

    public void removeAllTestData() {
        userService.deleteAllUsers(TestUser.ADMIN_1.toUserObject());
        pollService.deleteAllPolls(TestUser.ADMIN_1.toUserObject());
    }

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
