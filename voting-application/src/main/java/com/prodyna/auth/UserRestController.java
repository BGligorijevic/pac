package com.prodyna.auth;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j;

/**
 * User Rest controller handling login requests.
 */
@RestController
@RequestMapping(value = "/user")
@Log4j
public class UserRestController {

    private final Map<String, List<String>> userDb = new HashMap<>();

    public UserRestController() {
	userDb.put("tom", Arrays.asList("user"));
	userDb.put("sally", Arrays.asList("user", "admin"));
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public LoginResponse login(@RequestBody final User user) throws ServletException {
	if (user.getUserName() == null || !userDb.containsKey(user.getUserName())) {
	    throw new ServletException("Invalid login");
	}

	String token = Jwts.builder().setSubject(user.getUserName()).claim("roles", userDb.get(user.getUserName()))
		.setIssuedAt(new Date()).signWith(SignatureAlgorithm.HS256, "secretkey").compact();
	LoginResponse response = new LoginResponse();
	response.setToken(token);

	return response;
    }
}
