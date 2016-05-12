package com.prodyna.voting.vote;

import com.prodyna.voting.auth.user.Role;
import com.prodyna.voting.auth.user.User;
import com.prodyna.voting.common.Reject;
import com.prodyna.voting.poll.Poll;
import com.prodyna.voting.poll.PollOption;
import com.prodyna.voting.poll.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VoteServiceImpl implements VoteService {

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private PollService pollService;

    @Override
    public void vote(String pollId, String optionId, User user) {
        Reject.ifNull(pollId, "No poll id specified.");
        Reject.ifNull(optionId, "No option id specified.");
        Reject.ifNull(user, "No user specified.");
        Reject.ifNull(user.getUserId(), "No user id specified.");

        validateOption(pollId, optionId);
        validateAlreadyVoted(user.getUserId(), pollId);

        voteRepository.insert(new Vote(UUID.randomUUID().toString(), pollId, user.getUserId(), optionId));
    }

    private void validateAlreadyVoted(String userId, String pollId) {
        List<Vote> votes = voteRepository.findByUserIdAndPollId(userId, pollId);
        if (!votes.isEmpty()) {
            throw new IllegalArgumentException("User has already voted on this poll.");
        }
    }

    @Override
    public VotingResults getPollResults(String pollId, User user) {
        Reject.always("To be implemented!");
        return null;
    }

    @Override
    public List<Vote> getUserVotes(User user) {
        return voteRepository.findByUserId(user.getUserId());
    }

    @Override
    public void deleteAllVotes(User user) {
        if (!Role.isUserAdmin(user)) {
            Reject.always("User is not allowed to delete all polls.");
        }
        voteRepository.deleteAll();
    }

    private void validateOption(String pollId, String optionId) {
        Optional<Poll> poll = pollService.getPoll(pollId);
        Reject.ifAbsent(poll, "No such poll exists.");

        boolean optionFound = false;

        for (PollOption pollOption : poll.get().getPollOptions()) {
            if (pollOption.get_id().equals(optionId)) {
                optionFound = true;
                break;
            }
        }

        Reject.iF(optionFound == false, "No such option found.");
    }
}
