package com.prodyna.voting.poll.helper;

import com.prodyna.voting.datagenerator.sampledata.TestPoll;
import com.prodyna.voting.datagenerator.sampledata.TestUser;
import com.prodyna.voting.poll.Vote;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Sample test vote data.
 */
@Getter
@AllArgsConstructor
public enum TestVote {

    VOTE_FOR_VANILLA("12345", TestPoll.ICE_CREAM.get_id(), TestUser.USER_1,
            TestPoll.ICE_CREAM.getPollOptions().get(0).get_id()),

    VOTE_FOR_SNICKERS("645456", TestPoll.ICE_CREAM.get_id(), TestUser.USER_2,
            TestPoll.ICE_CREAM.getPollOptions().get(1).get_id()),

    VOTE_FOR_VANILLA_USER_2("645456", TestPoll.ICE_CREAM.get_id(), TestUser.USER_2,
            TestPoll.ICE_CREAM.getPollOptions().get(1).get_id()),

    VOTE_FOR_SNICKERS_2("852424", TestPoll.ICE_CREAM.get_id(), TestUser.ADMIN_1,
            TestPoll.ICE_CREAM.getPollOptions().get(1).get_id());

    private String _id;
    private String pollId;
    private TestUser user;
    private String optionId;

    public Vote toVoteObject() {
        Vote vote = new Vote();
        vote.set_id(this._id);
        vote.setUser(this.user.getUsername());
        vote.setOptionId(this.optionId);

        return vote;
    }

    /**
     * All regular votes for testing. Special ones excluded.
     */
    public static final TestVote[] ALL_VOTES = new TestVote[]{VOTE_FOR_VANILLA, VOTE_FOR_SNICKERS, VOTE_FOR_SNICKERS_2};

    public static List<Vote> toVoteObjects(TestVote... testVotes) {
        List<Vote> votes = new ArrayList<>();
        for (TestVote testVote : testVotes) {
            votes.add(testVote.toVoteObject());
        }

        return votes;
    }
}
