package com.prodyna.voting.poll;

import com.prodyna.voting.auth.user.Role;
import com.prodyna.voting.auth.user.User;
import com.prodyna.voting.common.NumberUtils;
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
            pollOption.setPollId(poll.get_id());
        });

        return pollRepository.insert(poll);
    }

    @Override
    public Poll editPoll(Poll poll, User user) {
        return changePoll(poll, user, false);
    }

    private Poll changePoll(Poll poll, User user, boolean vote) {
        validatePoll(poll);
        Reject.ifNull(poll.get_id(), "No poll id specified.");
        Reject.ifNull(user, "No user specified.");

        Poll oldPoll = pollRepository.findOne(poll.get_id());
        Reject.ifNull(oldPoll, "No Poll with such ID exists.");

        if (!vote && !pollChangeAllowed(user, poll) && !Role.isUserAdmin(user)) {
            Reject.always("User is not allowed to change this poll.");
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
        Reject.ifNull(user, "No user specified.");

        Poll poll = pollRepository.findOne(id);
        Reject.ifNull(poll, "No such poll exists.");

        if (!pollChangeAllowed(user, poll) && !Role.isUserAdmin(user)) {
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

    @Override
    public Poll vote(String pollId, String optionId, User user) {
        Reject.ifNull(pollId, "No poll id specified.");
        Reject.ifNull(optionId, "No option id specified.");
        Reject.ifNull(user, "No user specified.");
        Reject.ifNull(user.getUserName(), "No user id specified.");

        Optional<Poll> poll = this.getPoll(pollId);
        Reject.ifAbsent(poll, "No such poll exists.");

        List<PollOption> pollOptions = poll.get().getPollOptions();
        int votesNumber = validateAlreadyVotedAndGetNumberOfVotes(pollOptions, user.getUserName());

        boolean optionFound = false;
        for (PollOption pollOption : pollOptions) {
            if (pollOption.get_id().equals(optionId)) {
                optionFound = true;
                addVote(pollOption, user);
            }
            pollOption.setPercentage(calculatePercentage(pollOption, votesNumber));
        }
        Reject.ifFalse(optionFound, "No such poll option exists.");

        return changePoll(poll.get(), user, true);
    }

    private float calculatePercentage(PollOption pollOption, float votesNumber) {
        return NumberUtils.round2Decimals((float) pollOption.getVotes().size() / votesNumber * 100);
    }

    private void addVote(PollOption pollOption, User user) {
        Vote vote = new Vote(pollOption.get_id());
        vote.set_id(UUID.randomUUID().toString());
        vote.setUser(user.getUserName());
        vote.setChangeDate(new Date());
        pollOption.getVotes().add(vote);
    }

    private int validateAlreadyVotedAndGetNumberOfVotes(List<PollOption> pollOptions, String userName) {
        int numberVotes = 0;
        for (PollOption pollOption : pollOptions) {
            for (Vote vote : pollOption.getVotes()) {
                if (vote.getUser().equalsIgnoreCase(userName)) {
                    Reject.always("User has already voted on this poll. ");
                }
                numberVotes++;
            }
        }
        // the one to be voted will add one vote onto existing count
        return numberVotes + 1;
    }

    private boolean pollChangeAllowed(User user, Poll poll) {
        return poll.getCreator().equals(user.getUserName());
    }

    private void validatePoll(Poll poll) {
        Reject.ifNull(poll, "Poll must be non-null.");
        Reject.ifLessElementsThan(poll.getPollOptions(), 2, "Poll must have at least 2 options.");
    }
}
