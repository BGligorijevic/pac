package com.prodyna.voting.vote;

import com.prodyna.voting.auth.user.Role;
import com.prodyna.voting.auth.user.User;
import com.prodyna.voting.poll.Poll;
import com.prodyna.voting.poll.PollOption;
import com.prodyna.voting.poll.PollService;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
public class VoteServiceImplTest {

    @InjectMocks
    private VoteServiceImpl voteService;

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private PollService pollService;

    private static final String POLL_ID = "12345";
    private static final String BMW_OPTION_ID = "1";
    private static final String MERCEDES_OPTION_ID = "2";
    private static final String USER_ID = "7777";

    /**
     * Test for {@link VoteServiceImpl#vote(String, String, User)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void voteIsDenied() {
        String pollId = "";
        String optionId = "";
        User user = new User();

        voteService.vote(pollId, optionId, user);
    }

    /**
     * Test for {@link VoteServiceImpl#vote(String, String, User)}.
     */
    @Test
    public void voteIsSavedSuccessfully() {
        Poll carPoll = buildPoll();
        User user = buildUser();

        when(pollService.getPoll(carPoll.get_id())).thenReturn(Optional.of(carPoll));

        voteService.vote(carPoll.get_id(), MERCEDES_OPTION_ID, user);

        verify(voteRepository).insert(argThat(voteWithCorrectData()));
    }

    /**
     * Test for {@link VoteServiceImpl#getPollResults(String, User)}.
     */
    @Test
    public void voteResultsAreReturnedProperly() {
        Poll carPoll = buildPoll();
        User user = buildUser();
        List<Vote> votes = buildVotes();

        when(pollService.getPoll(carPoll.get_id())).thenReturn(Optional.of(carPoll));
        when(voteRepository.findByPollId(carPoll.get_id())).thenReturn(votes);

        VotingResults pollResults = voteService.getPollResults(POLL_ID, user);
        assertTrue(pollResults != null);
        assertTrue(pollResults.getPollId().equals(POLL_ID));
        assertTrue(pollResults.getVotingOptionResults() != null);
        assertTrue(pollResults.getVotingOptionResults().size() == 2);

        boolean allOptionsChecked = true;

        for (VotingOptionResult votingOptionResult : pollResults.getVotingOptionResults()) {
            if (votingOptionResult.getOptionId().equals(BMW_OPTION_ID)) {
                assertTrue(votingOptionResult.getCountVotes() == 2);
                assertTrue(votingOptionResult.getPercentage() == 100);
            } else if (votingOptionResult.getOptionId().equals(MERCEDES_OPTION_ID)) {
                assertTrue(votingOptionResult.getCountVotes() == 0);
                assertTrue(votingOptionResult.getPercentage() == 0);
            } else {
                allOptionsChecked = false;
            }
        }

        assertTrue(allOptionsChecked);
    }

    private Poll buildPoll() {
        List<PollOption> pollOptions = new ArrayList<>();
        PollOption pollOption1 = new PollOption("BMW");
        pollOption1.set_id(BMW_OPTION_ID);

        PollOption pollOption2 = new PollOption("Mercedes");
        pollOption2.set_id(MERCEDES_OPTION_ID);

        pollOptions.add(pollOption1);
        pollOptions.add(pollOption2);

        Poll carPoll = new Poll();
        carPoll.set_id(POLL_ID);
        carPoll.setTitle("Which is your favourite car?");
        carPoll.setPollOptions(pollOptions);

        return carPoll;
    }

    private User buildUser() {
        User user = new User();
        user.setUserId(USER_ID);
        user.setRole(Role.ADMINISTRATOR);

        return user;
    }

    private List<Vote> buildVotes() {
        List<Vote> votes = new ArrayList<>();
        Vote bmwVote1 = new Vote();
        bmwVote1.setPollId(POLL_ID);
        bmwVote1.setOptionId(BMW_OPTION_ID);
        bmwVote1.setUserId(USER_ID);

        Vote bmwVote2 = new Vote();
        bmwVote2.setPollId(POLL_ID);
        bmwVote2.setOptionId(BMW_OPTION_ID);
        bmwVote2.setUserId(USER_ID);

        votes.add(bmwVote1);
        votes.add(bmwVote2);

        return votes;
    }

    private Matcher<Vote> voteWithCorrectData() {
        return new TypeSafeMatcher<Vote>() {
            public boolean matchesSafely(Vote vote) {
                return MERCEDES_OPTION_ID.equals(vote.getOptionId()) && POLL_ID.equals(vote.getPollId())
                        && USER_ID.equals(vote.getUserId());
            }

            public void describeTo(Description description) {
                description.appendText("Should match vote with correct data");
            }
        };
    }
}
