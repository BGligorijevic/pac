package com.prodyna.voting.vote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VotingServiceImpl implements VotingService {

    @Autowired
    private VotingRepository votingRepository;

    @Override
    public List<Vote> getAllVotes() {
        return votingRepository.findAll();
    }
}
