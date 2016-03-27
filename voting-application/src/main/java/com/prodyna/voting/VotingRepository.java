package com.prodyna.voting;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface VotingRepository extends MongoRepository<Vote, Long> {

}
