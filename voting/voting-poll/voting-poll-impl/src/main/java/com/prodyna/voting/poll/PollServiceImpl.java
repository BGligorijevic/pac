package com.prodyna.voting.poll;

import com.prodyna.voting.auth.user.Role;
import com.prodyna.voting.auth.user.User;
import com.prodyna.voting.common.Reject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PollServiceImpl implements PollService {

    @Autowired
    private PollRepository pollRepository;

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
