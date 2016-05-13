package com.prodyna.voting.vote.helper;

import com.prodyna.voting.auth.helper.TestUser;
import com.prodyna.voting.poll.helper.TestPoll;
import com.prodyna.voting.vote.Vote;
import lombok.AllArgsConstructor;
import lombok.Getter;

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

    VOTE_FOR_SNICKERS_2("852424", TestPoll.ICE_CREAM.get_id(), TestUser.ADMIN_1,
            TestPoll.ICE_CREAM.getPollOptions().get(1).get_id());

    private String _id;
    private String pollId;
    private TestUser user;
    private String optionId;

    public Vote toVoteObject() {
        Vote vote = new Vote();
        vote.set_id(this._id);
        vote.setUserId(this.user.getUserId());
        vote.setPollId(this.pollId);
        vote.setOptionId(this.optionId);

        return vote;
    }

    /**
     * All regular votes for testing. Special ones excluded.
     */
    public static final TestVote[] ALL_VOTES = new TestVote[]{VOTE_FOR_VANILLA, VOTE_FOR_SNICKERS, VOTE_FOR_SNICKERS_2};
}
