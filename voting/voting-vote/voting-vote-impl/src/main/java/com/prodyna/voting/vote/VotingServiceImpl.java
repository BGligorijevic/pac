package com.prodyna.voting.vote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VotingServiceImpl implements VotingService {

    @Autowired
    private VotingRepository votingRepository;

    @Override
    public List<Vote> getAllVotes() {
        return votingRepository.findAll();
    }

    @Override
    public Optional<Vote> getVote(String id) {
        Vote vote = votingRepository.findByVoteId(id);
        return Optional.ofNullable(vote);
    }
}
