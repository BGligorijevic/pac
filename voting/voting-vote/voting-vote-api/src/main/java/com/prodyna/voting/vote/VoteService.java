package com.prodyna.voting.vote;

import com.prodyna.voting.auth.user.User;

import java.util.List;

/**
 * Vote API.
 */
public interface VoteService {

    /**
     * Saves a vote of the specified user.
     *
     * @param pollId   Poll id
     * @param optionId Id of the option to vote for
     * @param user User creating the vote
     */
    void vote(String pollId, String optionId, User user);

    /**
     * Returns the results of the voting for specified poll.
     *
     * @param pollId Poll id
     * @param user
     * @return VotingResults containing all relevant voting data
     * @throws IllegalArgumentException if user did not vote on this poll (administrator are always allowed)
     *                                  or poll cannot be found.
     */
    VotingResults getPollResults(String pollId, User user);

    /**
     * Returns all votes of the specified user.
     *
     * @param user User who owns the votes
     * @return Votes of the specified user
     */
    List<Vote> getUserVotes(User user);

    /**
     * Deletes all votes.
     *
     * @param user User performing the call
     * @throws IllegalArgumentException if user has no permission to delete the polls
     */
    void deleteAllVotes(User user);
}
