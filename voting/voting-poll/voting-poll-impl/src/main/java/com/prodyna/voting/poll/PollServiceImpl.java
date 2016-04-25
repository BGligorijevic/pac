package com.prodyna.voting.poll;

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
    public Optional<Poll> getPoll(String pollId) {
        Poll poll = pollRepository.findByPollId(pollId);
        return Optional.ofNullable(poll);
    }
}
