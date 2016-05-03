package com.prodyna.voting.poll;

import com.prodyna.voting.auth.user.Role;
import com.prodyna.voting.auth.user.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class PollServiceImplTest {

    private static final String USER_ADMIN_ID = "12345";
    private static final String USER_ID_1 = "777888";
    private static final String USER_ID_2 = "43636363";
    private static final String VOTE_ID = UUID.randomUUID().toString();

    @InjectMocks
    private PollServiceImpl pollService;

    @Mock
    private PollRepository pollRepository;

    /**
     * Test for {@link PollService#getAllPolls()}.
     */
    @Test
    public void getAllPollsReturnsCorrectNumberOfVotes() {
        User normalUser = createNormalUser(USER_ID_1);
        List<Poll> polls = new ArrayList<>();
        Poll poll1 = createVote("744424", "How much do you earn?", normalUser);
        Poll poll2 = createVote("185429", "Who will you poll for?", normalUser);
        polls.add(poll1);
        polls.add(poll2);

        when(pollRepository.findAll()).thenReturn(polls);

        List<Poll> resultPolls = pollService.getAllPolls();
        assertTrue(resultPolls.size() == 2);
    }

    /**
     * Test for {@link PollService#getPoll(String)}.
     */
    @Test
    public void getPollReturnsPoll() {
        Poll poll = createVote(VOTE_ID, "Your favourite OS?", createNormalUser(USER_ID_1));

        when(pollRepository.findOne(VOTE_ID)).thenReturn(poll);

        Optional<Poll> voteResult = pollService.getPoll(VOTE_ID);
        assertTrue(voteResult.isPresent());
    }

    /**
     * Test for {@link PollService#getPoll(String)}.
     */
    @Test
    public void getPollReturnsAbsent() {
        when(pollRepository.findOne(VOTE_ID)).thenReturn(null);
        Optional<Poll> voteResult = pollService.getPoll(VOTE_ID);
        assertFalse(voteResult.isPresent());
    }

    /**
     * Test for {@link PollService#deletePoll(String, User)}.
     */
    @Test
    public void pollIsDeletedWhenPollIsHis() {
        User normalUser = createNormalUser(USER_ID_1);
        Poll poll = createVote(VOTE_ID, "Your favourite OS?", createNormalUser(USER_ID_1));

        when(pollRepository.findOne(VOTE_ID)).thenReturn(poll);

        pollService.deletePoll(poll.get_id(), normalUser);

        verify(pollRepository).delete(poll);
    }


    /**
     * Test for {@link PollService#deletePoll(String, User)}.
     */
    @Test
    public void pollIsDeletedAlwaysWhenUserIsAdmin() {
        Poll poll = createVote(VOTE_ID, "Your favourite OS?", createNormalUser(USER_ID_2));

        when(pollRepository.findOne(VOTE_ID)).thenReturn(poll);
        pollService.deletePoll(poll.get_id(), createUser(true, USER_ADMIN_ID));

        verify(pollRepository).delete(poll);
    }

    /**
     * Test for {@link PollService#deletePoll(String, User)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void pollIsNotDeletedWhenUserIsNormalUserAndPollIsNotHis() {
        Poll poll = createVote(VOTE_ID, "Your favourite OS?", createNormalUser(USER_ID_1));

        when(pollRepository.findOne(VOTE_ID)).thenReturn(poll);
        pollService.deletePoll(poll.get_id(), createNormalUser(USER_ID_2));
    }

    /**
     * Test for {@link PollService#deletePoll(String, User)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void pollIsNotDeletedWhenPollIsNotFoundById() {
        when(pollRepository.findOne(VOTE_ID)).thenReturn(null);
        pollService.deletePoll(VOTE_ID, createNormalUser(USER_ID_1));
    }

    private Poll createVote(String id, String title, User author) {
        Poll poll = new Poll();

        poll.setAuthor(author);
        poll.set_id(id);
        poll.setTitle(title);
        poll.setDescription(title);
        poll.setChangeDate(new Date());

        return poll;
    }

    private User createNormalUser(String id) {
        return createUser(false, id);
    }

    private User createUser(boolean isAdmin, String id) {
        User user = new User();
        user.setUserId(id);

        if (isAdmin) {
            user.setUserName("admin");
            user.setPassword("_admin");
            user.setRole(Role.ADMINISTRATOR);
        } else {
            user.setUserName("user");
            user.setPassword("_user");
            user.setRole(Role.USER);
        }

        return user;
    }
}
