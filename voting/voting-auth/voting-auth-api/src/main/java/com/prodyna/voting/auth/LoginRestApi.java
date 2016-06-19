package com.prodyna.voting.auth;

import com.prodyna.voting.auth.user.User;
import com.prodyna.voting.auth.user.UserService;
import com.prodyna.voting.common.Reject;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public User login(@RequestBody final User user) {
        Optional<User> loginResult = userService.login(user);
        Reject.ifAbsent(loginResult, "Incorrect credentials.");

        return loginResult.get();
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(IllegalArgumentException.class)
    private void handleLoginFailure(final IllegalArgumentException exception) {
        log.debug("Login failed.", exception);
    }
}
