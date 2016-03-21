package com.prodyna.auth.user;

import java.util.Optional;

public interface UserService {

    Optional<User> findUserByUserNameAndPassword(String userName, String password);
}
