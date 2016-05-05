package com.prodyna.voting.vote;

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

        User user = new User();
        user.setUserId(USER_ID);

        when(pollService.getPoll(carPoll.get_id())).thenReturn(Optional.of(carPoll));

        voteService.vote(carPoll.get_id(), pollOption2.get_id(), user);

        verify(voteRepository).insert(argThat(voteWithCorrectData()));
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
