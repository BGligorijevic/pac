package com.prodyna.voting.auth;

import com.prodyna.voting.auth.user.User;
import com.prodyna.voting.auth.user.UserService;
import com.prodyna.voting.common.Reject;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

/**
 * Rest controller handling login requests.
 */
@RestController
@RequestMapping(value = "/user")
@Log4j
public class LoginRestApi {

    @Autowired
    private UserService userService;

    @Value("${voting.app.secret.key}")
    @Getter
    private String secretKey;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String login(@RequestBody final User user) {
        Reject.ifNull(user, "Invalid login data");

        Optional<User> dbUser = userService.findUserByUserNameAndPassword(user.getUserName(), user.getPassword());
        Reject.ifAbsent(dbUser, "Invalid credentials");

        User foundUser = dbUser.get();

        String token = Jwts.builder().setSubject(foundUser.getUserName()).claim("role", foundUser.getRole())
                .setIssuedAt(new Date()).signWith(SignatureAlgorithm.HS256, secretKey).compact();

        return token;
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(IllegalArgumentException.class)
    private void handleLoginFailure(final IllegalArgumentException exception) {
        log.debug("Login failed.", exception);
    }
}
