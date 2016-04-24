package com.prodyna.voting.vote;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface VotingRepository extends MongoRepository<Vote, String> {

    Vote findByVoteId(String voteId);
}
