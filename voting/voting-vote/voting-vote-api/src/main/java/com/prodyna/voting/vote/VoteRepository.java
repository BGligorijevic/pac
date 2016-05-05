package com.prodyna.voting.vote;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface VoteRepository extends MongoRepository<Vote, String> {

}
