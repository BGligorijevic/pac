package com.prodyna.voting.auth.user;

import java.util.Optional;

public interface UserService {

    Optional<User> findUserByUserNameAndPassword(String userName, String password);

    void saveUser(User user);
}
