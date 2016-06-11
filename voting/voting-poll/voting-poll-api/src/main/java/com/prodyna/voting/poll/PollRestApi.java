package com.prodyna.voting.poll;

import com.prodyna.voting.auth.user.User;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Poll Rest controller handling all incoming Rest requests.
 */
@RestController
@RequestMapping(value = "/api/polls")
@Log4j
public class PollRestApi {

    @Autowired
    private PollService pollService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Poll> getAllPolls() {
        return pollService.getAllPolls();
    }

    @RequestMapping(method = RequestMethod.POST)
    public Poll createPoll(@RequestBody Poll poll) {
        return pollService.createPoll(poll);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Poll editPoll(@RequestBody Poll poll, HttpServletRequest request) {
        return pollService.editPoll(poll, userFromRequest(request));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Poll getPoll(@PathVariable String id) {
        Optional<Poll> poll = pollService.getPoll(id);
        if (!poll.isPresent()) {
            throw new NoSuchElementException();
        }

        return poll.get();
    }

    @RequestMapping(value = "/vote/{pollId}/{optionId}", method = RequestMethod.POST)
    public Poll vote(@PathVariable String pollId, @PathVariable String optionId, HttpServletRequest request) {
        return pollService.vote(pollId, optionId, userFromRequest(request));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deletePoll(@PathVariable String id, HttpServletRequest request) {
        pollService.deletePoll(id, userFromRequest(request));
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    @ExceptionHandler(IllegalArgumentException.class)
    private void handleForbidden(final IllegalArgumentException exception) {
        log.debug("Resource denied.", exception);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    private void handleVoteNotFound(final NoSuchElementException exception) {
        log.debug("Poll not found.", exception);
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
