package com.prodyna.voting.auth.user;

import java.util.Optional;

public interface UserService {

    /**
     * Returns the user name from the database for the specified userName.
     * Returns {@link Optional#empty()} if such user is not found.
     *
     * @param userName user name of the user
     * @return user wrapped in {@link Optional} or {@link Optional#empty()}.
     */
    Optional<User> findUserByUserName(String userName);

    /**
     * Tries to login the user with specified password, returning the appropriate result.
     *
     * @param userToLogin User with all information
     * @return user object if login succeeded, {@link Optional#empty()} if login failed.
     */
    Optional<User> login(User userToLogin);

    /**
     * Saves the user with hashed password.
     *
     * @param user User
     * @return Saved user
     * @throws IllegalArgumentException if provided data is incomplete (missing userName or role etc)
     */
    User saveUser(User user);

    /**
     * Deletes all users.
     *
     * @param user User making the call.
     */
    void deleteAllUsers(User user);
}
