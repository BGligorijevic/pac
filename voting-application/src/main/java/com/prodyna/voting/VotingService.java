package com.prodyna.voting;

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
