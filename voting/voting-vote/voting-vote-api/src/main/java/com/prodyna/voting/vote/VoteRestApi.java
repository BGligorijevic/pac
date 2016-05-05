package com.prodyna.voting.vote;


import com.prodyna.voting.auth.RestResources;
import com.prodyna.voting.auth.user.User;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Vote Rest controller handling all incoming Rest requests.
 */
@RestController
@RequestMapping(value = RestResources.UrlPaths.VOTE_URL)
@Log4j
public class VoteRestApi {

    @Autowired
    private VoteService voteService;

    @RequestMapping(value = "/{pollId}/{optionId}", method = RequestMethod.POST)
    public void vote(@PathVariable String pollId, @PathVariable String optionId, HttpServletRequest request) {
        voteService.vote(pollId, optionId, userFromRequest(request));
    }

    @RequestMapping(value = "/{pollId}/results", method = RequestMethod.GET)
    public void getPollResults(@PathVariable String pollId, HttpServletRequest request) {
        voteService.getPollResults(pollId, userFromRequest(request));
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler(IllegalArgumentException.class)
    private void handleForbidden(final IllegalArgumentException exception) {
        log.debug("Resource denied.", exception);
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    private void handleInternalError(final Exception exception) {
        log.error("Generic exception occurred.", exception);
    }

    private User userFromRequest(HttpServletRequest request) {
        return (User) request.getAttribute("user");
    }
}
