package com.prodyna.voting.poll;

import java.util.List;
import java.util.Optional;

/**
 * Poll API.
 */
public interface PollService {

    /**
     * Returns all polls.
     *
     * @return
     */
    List<Poll> getAllPolls();

    /**
     * Returns poll using ID.
     *
     * @return Poll
     */
    Optional<Poll> getPoll(String pollId);
}
