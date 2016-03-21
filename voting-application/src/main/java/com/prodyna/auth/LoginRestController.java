package com.prodyna.auth;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.prodyna.Application;
import com.prodyna.auth.user.User;
import com.prodyna.auth.user.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j;

/**
 * User Rest controller handling login requests.
 */
@RestController
@RequestMapping(value = "/user")
@Log4j
public class LoginRestController {

    @Autowired
    private Application application;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public LoginResponse login(@RequestBody final User user) {
	if (user == null) {
	    throw new IllegalArgumentException("Invalid login");
	}

	Optional<User> dbUser = userService.findUserByUserNameAndPassword(user.getUserName(), user.getPassword());

	if (!dbUser.isPresent()) {
	    log.debug("Invalid login for user: " + user);
	    throw new IllegalArgumentException("Invalid login");
	}

	User foundUser = dbUser.get();

	String token = Jwts.builder().setSubject(foundUser.getUserName()).claim("roles", foundUser.getRoles())
		.setIssuedAt(new Date()).signWith(SignatureAlgorithm.HS256, application.getSecretKey()).compact();
	LoginResponse response = new LoginResponse();
	response.setToken(token);

	return response;
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(IllegalArgumentException.class)
    private void handleLoginFailure(final IllegalArgumentException exception) {
	log.debug("Login failed.", exception);
    }
}
