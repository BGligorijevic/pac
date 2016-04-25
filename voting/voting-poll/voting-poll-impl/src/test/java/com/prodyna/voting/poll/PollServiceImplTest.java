package com.prodyna.voting.poll;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class PollServiceImplTest {

    private static final String VOTE_ID = UUID.randomUUID().toString();

    @InjectMocks
    private PollServiceImpl votingService;

    @Mock
    private PollRepository pollRepository;

    /**
     * Test for {@link PollService#getAllPolls()}.
     */
    @Test
    public void getAllPollsReturnsCorrectNumberOfVotes() {
        List<Poll> polls = new ArrayList<>();
        Poll poll1 = createVote("744424", "How much do you earn?");
        Poll poll2 = createVote("185429", "Who will you poll for?");
        polls.add(poll1);
        polls.add(poll2);

        when(pollRepository.findAll()).thenReturn(polls);

        List<Poll> resultPolls = votingService.getAllPolls();
        assertTrue(resultPolls.size() == 2);
    }

    /**
     * Test for {@link PollService#getPoll(String)}.
     */
    @Test
    public void getPollReturnsPoll() {
        Poll poll = createVote(VOTE_ID, "Your favourite OS?");

        when(pollRepository.findByPollId(VOTE_ID)).thenReturn(poll);

        Optional<Poll> voteResult = votingService.getPoll(VOTE_ID);
        assertTrue(voteResult.isPresent());
    }

    /**
     * Test for {@link PollService#getPoll(String)}.
     */
    @Test
    public void getPollReturnsAbsent() {
        when(pollRepository.findOne(VOTE_ID)).thenReturn(null);
        Optional<Poll> voteResult = votingService.getPoll(VOTE_ID);
        assertFalse(voteResult.isPresent());
    }

    private Poll createVote(String voteId, String title) {
        Poll poll = new Poll();
        poll.setPollId(voteId);
        poll.setTitle(title);
        poll.setDescription(title);
        poll.setChangeDate(new Date());

        return poll;
    }
}
