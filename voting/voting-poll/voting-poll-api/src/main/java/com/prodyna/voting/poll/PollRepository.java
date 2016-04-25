package com.prodyna.voting.poll;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PollRepository extends MongoRepository<Poll, String> {

    Poll findByPollId(String pollId);
}
