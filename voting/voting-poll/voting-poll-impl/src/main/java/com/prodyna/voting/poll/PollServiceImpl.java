package com.prodyna.voting.poll;

import com.prodyna.voting.auth.user.Role;
import com.prodyna.voting.auth.user.User;
import com.prodyna.voting.common.Reject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PollServiceImpl implements PollService {

    @Autowired
    private PollRepository pollRepository;

    @Override
    public Poll createPoll(Poll poll) {
        validatePoll(poll);

        if (poll.getChangeDate() == null) {
            poll.setChangeDate(new Date());
        }

        poll.getPollOptions().stream().filter(pollOption -> pollOption.get_id() == null).forEach(pollOption -> {
            pollOption.set_id(UUID.randomUUID().toString());
        });

        return pollRepository.insert(poll);
    }

    @Override
    public Poll editPoll(Poll poll, User user) {
        validatePoll(poll);
        Reject.ifNull(poll.get_id(), "No poll id specified.");
        Reject.ifNull(user, "No user specified.");

        if (!pollBelongsToUser(user, poll) && !Role.isUserAdmin(user)) {
            Reject.always("User is not allowed to change this poll.");
        }

        Poll foundPoll = pollRepository.findOne(poll.get_id());
        Reject.ifNull(foundPoll, "No Poll with such ID exists.");

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
        Reject.ifNull(user, "No user specified.");

        Poll poll = pollRepository.findOne(id);
        Reject.ifNull(poll, "No such poll exists.");

        if (!pollBelongsToUser(user, poll) && !Role.isUserAdmin(user)) {
            Reject.always("User is not allowed to delete this poll.");
        }
        pollRepository.delete(poll);
    }

    @Override
    public void deleteAllPolls(User user) {
        if (!Role.isUserAdmin(user)) {
            Reject.always("User is not allowed to delete all polls.");
        }
        pollRepository.deleteAll();
    }

    private boolean pollBelongsToUser(User user, Poll poll) {
        return poll.getAuthorId().equals(user.getUserId());
    }

    private void validatePoll(Poll poll) {
        Reject.ifNull(poll, "Poll must be non-null.");
        Reject.ifLessElementsThan(poll.getPollOptions(), 2, "Poll must have at least 2 options.");
    }
}
