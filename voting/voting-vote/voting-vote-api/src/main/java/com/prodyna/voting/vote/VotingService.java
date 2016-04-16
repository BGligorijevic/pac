package com.prodyna.voting.vote;

import java.util.List;

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

}
