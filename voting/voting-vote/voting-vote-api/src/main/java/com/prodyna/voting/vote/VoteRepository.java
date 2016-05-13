package com.prodyna.voting.vote;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface VoteRepository extends MongoRepository<Vote, String> {

    List<Vote> findByUserId(String userId);

    List<Vote> findByUserIdAndPollId(String userId, String pollId);

    List<Vote> findByPollId(String pollId);
}
