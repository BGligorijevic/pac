package com.prodyna.voting.poll;

import com.prodyna.voting.auth.user.User;

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

    /**
     * Deletes the poll id.
     *
     * @param pollId
     * @param user
     * @throws IllegalArgumentException if poll cannot be found or
     *                                  user has no permission to delete the poll.
     */
    void deletePoll(String pollId, User user);
}
