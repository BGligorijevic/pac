package com.prodyna.voting.auth.user;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Manages lower level interaction with the persistence layer.
 * Note: Never inject implementation of this interface directly other than in the service itself.
 */
public interface UserRepository extends MongoRepository<User, Long> {

    /**
     * Looks up the user in the repository for the specified user name.
     * As per SpringData specs, the userName will be case sensitive.
     */
    User findByUserName(String userName);
}
