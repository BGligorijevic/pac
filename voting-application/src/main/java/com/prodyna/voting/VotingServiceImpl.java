package com.prodyna.voting;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VotingServiceImpl implements VotingService {

    @Autowired
    private VotingRepository votingRepository;

    @Override
    public List<Vote> getAllVotes() {
	return votingRepository.findAll();
    }
}
