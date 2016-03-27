package com.prodyna.auth.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> findUserByUserNameAndPassword(final String userName, final String password) {
	User user = userRepository.findByUserNameAndPassword(userName, password);
	return user != null ? Optional.of(user) : Optional.empty();
    }

    @Override
    public void saveUser(final User user) {
	userRepository.save(user);
    }
}
