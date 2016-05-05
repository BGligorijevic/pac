package com.prodyna.voting.poll.helper;

import com.prodyna.voting.auth.helper.TestUser;
import com.prodyna.voting.poll.Poll;
import com.prodyna.voting.poll.PollOption;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

/**
 * Sample test poll data.
 */
@Getter
@AllArgsConstructor
public enum TestPoll {

    ICE_CREAM("123", "Your favourite Ice Cream?", "Please choose your fav. ice cream!",
            TestUser.USER_1.getUserId(), new Date(),
            Arrays.asList(new PollOption[]{new PollOption("Vanilla"), new PollOption("Snickers")})),
    OS("311", "Your favourite OS?", "Please choose best Operating System.",
            TestUser.USER_1.getUserId(), new Date(),
            Arrays.asList(new PollOption[]{new PollOption("Windows"), new PollOption("Ubuntu"), new PollOption("MacOS")})),
    CAR("656", "Best car ever?", "Please vote the best car ever made.",
            TestUser.USER_2.getUserId(), new Date(),
            Arrays.asList(new PollOption[]{new PollOption("BMW"), new PollOption("Audi"), new PollOption("Ferrari")})),
    CHANGED_ICE_CREAM("123", "Your favourite Ice Cream?", "Please choose your favourite ice cream flavour.",
            TestUser.USER_1.getUserId(), new Date(),
            Arrays.asList(new PollOption[]{new PollOption("Vanilla"), new PollOption("Snickers"), new PollOption("Chocolate")}));

    private String _id;
    private String title;
    private String description;
    private String authorId;
    private Date changeDate;
    private List<PollOption> pollOptions;

    public Poll toPollObject() {
        Poll poll = new Poll();
        poll.set_id(this._id);
        poll.setTitle(this.title);
        poll.setDescription(this.description);
        poll.setChangeDate(this.changeDate);
        poll.setPollOptions(this.pollOptions);
        poll.setAuthorId(this.authorId);

        return poll;
    }

    /**
     * All regular polls for testing. Special ones excluded.
     */
    public static final TestPoll[] ALL_POLLS = new TestPoll[]{ICE_CREAM, OS, CAR};
}
