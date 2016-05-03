package com.prodyna.voting.poll.helper;

import com.prodyna.voting.auth.helper.TestUser;
import com.prodyna.voting.auth.user.User;
import com.prodyna.voting.poll.Poll;
import com.prodyna.voting.poll.PollOptions;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Sample test poll data.
 */
@Getter
@AllArgsConstructor
public enum TestPoll {

    ICE_CREAM("123", "Your favourite Ice Cream?", "Please choose your fav. ice cream!",
            TestUser.USER_1.toUserObject(), new Date(),
            Arrays.asList(new PollOptions[]{new PollOptions("Vanilla"), new PollOptions("Snickers")})),
    OS("311", "Your favourite OS?", "Please choose best Operating System.",
            TestUser.USER_1.toUserObject(), new Date(),
            Arrays.asList(new PollOptions[]{new PollOptions("Windows"), new PollOptions("Ubuntu"), new PollOptions("MacOS")})),
    CAR("656", "Best car ever?", "Please vote the best car ever made.",
            TestUser.USER_2.toUserObject(), new Date(),
            Arrays.asList(new PollOptions[]{new PollOptions("BMW"), new PollOptions("Audi"), new PollOptions("Ferrari")}));

    private String _id;
    private String title;
    private String description;
    private User author;
    private Date changeDate;
    private List<PollOptions> pollOptions;

    public Poll toPollObject() {
        Poll poll = new Poll();
        poll.set_id(this._id);
        poll.setTitle(this.title);
        poll.setDescription(this.description);
        poll.setChangeDate(this.changeDate);
        poll.setPollOptions(this.pollOptions);
        poll.setAuthor(this.author);

        return poll;
    }
}
