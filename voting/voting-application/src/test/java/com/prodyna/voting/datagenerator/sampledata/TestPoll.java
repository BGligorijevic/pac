package com.prodyna.voting.datagenerator.sampledata;

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

    ICE_CREAM("123", "Your favourite Ice Cream?", "Please choose your fav. ice cream.",
            TestUser.USER_1.getUsername(), new Date(),
            Arrays.asList(new PollOption[]{new PollOption("Vanilla", "123"), new PollOption("Snickers", "123")})),
    OS("311", "Your favourite OS?", "Please choose the best operating system.",
            TestUser.USER_1.getUsername(), new Date(),
            Arrays.asList(new PollOption[]{new PollOption("Windows", "311"), new PollOption("Ubuntu", "311"), new PollOption("MacOS", "311")})),
    CAR("656", "Which is the best car ever?", "Please vote the best car ever made.",
            TestUser.USER_2.getUsername(), new Date(),
            Arrays.asList(new PollOption[]{new PollOption("BMW", "656"), new PollOption("Audi", "656"), new PollOption("Ferrari", "656")})),
    CHANGED_ICE_CREAM("123", "Which is your favourite ice cream?", "Please choose your favourite ice cream flavour.",
            TestUser.USER_1.getUsername(), new Date(),
            Arrays.asList(new PollOption[]{new PollOption("Vanilla", "123"), new PollOption("Snickers", "123"), new PollOption("Chocolate", "123")}));

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
        poll.setCreator(this.authorId);

        return poll;
    }

    /**
     * All regular polls for testing. Special ones excluded.
     */
    public static final TestPoll[] ALL_POLLS = new TestPoll[]{ICE_CREAM, OS, CAR};
}
