package com.prodyna.voting.vote;

import java.util.List;
import java.util.Optional;

/**
 * Voting API.
 */
public interface VotingService {

    /**
     * Returns all votes.
     *
     * @return
     */
    List<Vote> getAllVotes();

    /**
     * Returns vote using ID.
     *
     * @return Vote
     */
    Optional<Vote> getVote(String voteId);
}
