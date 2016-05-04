package com.prodyna.voting.poll;

import com.prodyna.voting.auth.user.Role;
import com.prodyna.voting.auth.user.User;
import com.prodyna.voting.common.Reject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PollServiceImpl implements PollService {

    @Autowired
    private PollRepository pollRepository;

    @Override
    public Poll createPoll(Poll poll) {
        Reject.ifNull(poll, "Poll must be non-null.");
        Reject.ifNull(poll.getPollOptions(), "Poll must have poll options.");
        Reject.ifLessElementsThan(poll.getPollOptions(), 2, "Poll must have at least 2 options.");

        if (poll.getChangeDate() == null) {
            poll.setChangeDate(new Date());
        }

        return pollRepository.save(poll);
    }

    @Override
    public List<Poll> getAllPolls() {
        return pollRepository.findAll();
    }

    @Override
    public Optional<Poll> getPoll(String id) {
        Poll poll = pollRepository.findOne(id);
        return Optional.ofNullable(poll);
    }

    @Override
    public void deletePoll(String id, User user) {
        Poll poll = pollRepository.findOne(id);
        Reject.ifNull(poll, "No such poll exists.");

        if (!pollBelongsToUser(user, poll) && !Role.isUserAdmin(user)) {
            Reject.always("User is not allowed to delete this poll.");
        }
        pollRepository.delete(poll);
    }

    private boolean pollBelongsToUser(User user, Poll poll) {
        return poll.getAuthor().getUserId().equals(user.getUserId());
    }
}
