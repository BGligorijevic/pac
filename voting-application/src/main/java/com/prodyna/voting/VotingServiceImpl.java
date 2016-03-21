package com.prodyna.voting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class VotingServiceImpl implements VotingService {

    @Override
    public List<Vote> getAllVotes() {
	List<Vote> votes = new ArrayList<>();
	Vote vote = new Vote();
	vote.setTitle("Test title");
	vote.setDescription("Test description");
	votes.add(vote);

	return votes;
    }
}
