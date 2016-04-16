package com.prodyna.voting.auth.user;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, Long> {

    User findByUserNameAndPassword(String userName, String password);
}
