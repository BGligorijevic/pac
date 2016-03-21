package com.prodyna.auth.user;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public Optional<User> findUserByUserNameAndPassword(final String userName, final String password) {
	if (userName.equals("Tom") && password.equals("Jones")) {
	    User mockUser1 = new User();
	    mockUser1.setUserName("Tom");
	    mockUser1.setPassword("Jones");
	    mockUser1.setRoles(Arrays.asList(new Role[] { Role.NORMAL_USER }));

	    return Optional.of(mockUser1);
	}

	return Optional.empty();
    }

}
