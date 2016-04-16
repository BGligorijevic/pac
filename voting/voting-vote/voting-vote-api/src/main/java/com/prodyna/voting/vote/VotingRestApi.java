package com.prodyna.voting.vote;

import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Voting Rest controller handling all incoming Rest requests.
 */
@RestController
@RequestMapping(value = "/api/votes")
@Log4j
public class VotingRestApi {

    @Autowired
    private VotingService votingService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Vote> getAllVotes() {
        return votingService.getAllVotes();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Vote getVote() {
        throw new IllegalArgumentException("No permissions");
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler(IllegalArgumentException.class)
    private void handleForbidden(final IllegalArgumentException exception) {
        log.debug("Resource denied.", exception);
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    private void handleInternalError(final Exception exception) {
        log.error("Generic exception occured.", exception);
    }
}
