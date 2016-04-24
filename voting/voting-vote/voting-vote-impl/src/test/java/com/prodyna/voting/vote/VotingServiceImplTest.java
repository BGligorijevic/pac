package com.prodyna.voting.vote;

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
public class VotingServiceImplTest {

    private static final String VOTE_ID = UUID.randomUUID().toString();

    @InjectMocks
    private VotingServiceImpl votingService;

    @Mock
    private VotingRepository votingRepository;

    /**
     * Test for {@link VotingService#getAllVotes()}.
     */
    @Test
    public void getAllVotesReturnsCorrectNumberOfVotes() {
        List<Vote> votes = new ArrayList<>();
        Vote vote1 = createVote("744424", "How much do you earn?");
        Vote vote2 = createVote("185429", "Who will you vote for?");
        votes.add(vote1);
        votes.add(vote2);

        when(votingRepository.findAll()).thenReturn(votes);

        List<Vote> resultVotes = votingService.getAllVotes();
        assertTrue(resultVotes.size() == 2);
    }

    /**
     * Test for {@link VotingService#getVote(String)}.
     */
    @Test
    public void getVoteReturnsVote() {
        Vote vote = createVote(VOTE_ID, "Your favourite OS?");

        when(votingRepository.findByVoteId(VOTE_ID)).thenReturn(vote);

        Optional<Vote> voteResult = votingService.getVote(VOTE_ID);
        assertTrue(voteResult.isPresent());
    }

    /**
     * Test for {@link VotingService#getVote(String)}.
     */
    @Test
    public void getVoteReturnsAbsent() {
        when(votingRepository.findOne(VOTE_ID)).thenReturn(null);
        Optional<Vote> voteResult = votingService.getVote(VOTE_ID);
        assertFalse(voteResult.isPresent());
    }

    private Vote createVote(String voteId, String title) {
        Vote vote = new Vote();
        vote.setVoteId(voteId);
        vote.setTitle(title);
        vote.setDescription(title);
        vote.setCreated(new Date());

        return vote;
    }
}
