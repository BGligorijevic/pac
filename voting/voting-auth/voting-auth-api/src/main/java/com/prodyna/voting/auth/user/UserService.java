package com.prodyna.voting.auth.user;

import java.util.Optional;

public interface UserService {

    Optional<User> findUserByUserNameAndPassword(String userName, String password);

    /**
     * Saves the user.
     *
     * @param user User
     */
    void saveUser(User user);

    /**
     * Deletes all users.
     *
     * @param user User making the call.
     */
    void deleteAllUsers(User user);
}
