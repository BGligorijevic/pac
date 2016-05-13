package com.prodyna.voting.vote;

import com.prodyna.voting.auth.user.Role;
import com.prodyna.voting.auth.user.User;
import com.prodyna.voting.common.Reject;
import com.prodyna.voting.poll.Poll;
import com.prodyna.voting.poll.PollOption;
import com.prodyna.voting.poll.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
        Reject.ifNull(pollId, "No poll id specified.");
        Reject.ifNull(user, "No user specified.");
        Reject.ifNull(user.getUserId(), "No user id specified.");

        Optional<Poll> poll = pollService.getPoll(pollId);
        Reject.ifAbsent(poll, "No poll found with id " + pollId);

        validateUserPermission(pollId, user);

        Map<String, Integer> optionsWithCount = new HashMap<>();
        for (PollOption pollOption : poll.get().getPollOptions()) {
            optionsWithCount.put(pollOption.get_id(), 0);
        }

        List<Vote> votes = voteRepository.findByPollId(pollId);
        for (Vote vote : votes) {
            Integer count = optionsWithCount.get(vote.getOptionId());
            optionsWithCount.put(vote.getOptionId(), ++count);
        }

        List<VotingOptionResult> votingOptionResults = new ArrayList<>();
        for (Map.Entry<String, Integer> optionsWithCountEntry : optionsWithCount.entrySet()) {
            VotingOptionResult votingOptionResult = new VotingOptionResult();
            votingOptionResult.setOptionId(optionsWithCountEntry.getKey());
            votingOptionResult.setCountVotes(optionsWithCountEntry.getValue());

            double percentage = calculatePercentage(optionsWithCountEntry.getValue(), optionsWithCount.size());
            votingOptionResult.setPercentage(percentage);

            votingOptionResults.add(votingOptionResult);
        }

        return new VotingResults(pollId, votingOptionResults);
    }

    private double calculatePercentage(int numberOfVotes, int numberOfOptions) {
        return numberOfVotes / numberOfOptions * 100;
    }

    private void validateUserPermission(String pollId, User user) {
        if (!Role.isUserAdmin(user)) {
            List<Vote> vote = voteRepository.findByUserIdAndPollId(user.getUserId(), pollId);
            if (vote.size() != 1) {
                throw new IllegalArgumentException("Results can only be retrieved if the user has already voted.");
            }
        }
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
