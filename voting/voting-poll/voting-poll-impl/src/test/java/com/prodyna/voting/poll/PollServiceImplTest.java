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
    private static final String POLL_ID = UUID.randomUUID().toString();

    @InjectMocks
    private PollServiceImpl pollService;

    @Mock
    private PollRepository pollRepository;

    /**
     * Test for {@link PollService#getAllPolls()}.
     */
    @Test
    public void getAllPollsReturnsCorrectNumberOfVotes() {
        User normalUser = buildNormalUser(USER_ID_1);
        List<Poll> polls = new ArrayList<>();
        Poll poll1 = buildPoll("744424", "How much do you earn?", normalUser);
        Poll poll2 = buildPoll("185429", "Who will you poll for?", normalUser);
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
        Poll poll = buildPoll(POLL_ID, "Your favourite OS?", buildNormalUser(USER_ID_1));

        when(pollRepository.findOne(POLL_ID)).thenReturn(poll);

        Optional<Poll> voteResult = pollService.getPoll(POLL_ID);
        assertTrue(voteResult.isPresent());
    }

    /**
     * Test for {@link PollService#getPoll(String)}.
     */
    @Test
    public void getPollReturnsAbsent() {
        when(pollRepository.findOne(POLL_ID)).thenReturn(null);
        Optional<Poll> voteResult = pollService.getPoll(POLL_ID);
        assertFalse(voteResult.isPresent());
    }

    /**
     * Test for {@link PollService#deletePoll(String, User)}.
     */
    @Test
    public void pollIsDeletedWhenPollIsHis() {
        User normalUser = buildNormalUser(USER_ID_1);
        Poll poll = buildPoll(POLL_ID, "Your favourite OS?", buildNormalUser(USER_ID_1));

        when(pollRepository.findOne(POLL_ID)).thenReturn(poll);

        pollService.deletePoll(poll.get_id(), normalUser);

        verify(pollRepository).delete(poll);
    }

    /**
     * Test for {@link PollService#deletePoll(String, User)}.
     */
    @Test
    public void pollIsDeletedAlwaysWhenUserIsAdmin() {
        Poll poll = buildPoll(POLL_ID, "Your favourite OS?", buildNormalUser(USER_ID_2));

        when(pollRepository.findOne(POLL_ID)).thenReturn(poll);
        pollService.deletePoll(poll.get_id(), buildUser(true, USER_ADMIN_ID));

        verify(pollRepository).delete(poll);
    }

    /**
     * Test for {@link PollService#deletePoll(String, User)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void pollIsNotDeletedWhenUserIsNormalUserAndPollIsNotHis() {
        Poll poll = buildPoll(POLL_ID, "Your favourite OS?", buildNormalUser(USER_ID_1));

        when(pollRepository.findOne(POLL_ID)).thenReturn(poll);
        pollService.deletePoll(poll.get_id(), buildNormalUser(USER_ID_2));
    }

    /**
     * Test for {@link PollService#deletePoll(String, User)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void pollIsNotDeletedWhenPollIsNotFoundById() {
        when(pollRepository.findOne(POLL_ID)).thenReturn(null);
        pollService.deletePoll(POLL_ID, buildNormalUser(USER_ID_1));
    }

    /**
     * Test for {@link PollService#createPoll(Poll)}.
     */
    @Test
    public void pollIsCreated() {
        Poll poll = buildPoll("Which is your favourite color?", buildNormalUser(USER_ID_1));
        assertTrue(poll.get_id() == null);

        when(pollRepository.insert(poll)).thenReturn(poll);

        pollService.createPoll(poll);
        verify(pollRepository).insert(poll);
    }

    /**
     * Test for {@link PollService#createPoll(Poll)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void pollIsNotCreatedForNullInput() {
        pollService.createPoll(null);
    }

    /**
     * Test for {@link PollService#editPoll(Poll, User)}.
     */
    @Test
    public void pollIsEdited() {
        Poll poll = buildPoll("123456", "Which is your favourite color?", buildNormalUser(USER_ID_1));

        when(pollRepository.findOne(poll.get_id())).thenReturn(poll);
        when(pollRepository.save(poll)).thenReturn(poll);

        pollService.editPoll(poll, buildUser(true, USER_ADMIN_ID));

        verify(pollRepository).save(poll);
    }

    private Poll buildPoll(String id, String title, User author) {
        Poll poll = new Poll();

        poll.setAuthor(author);
        poll.set_id(id);
        poll.setTitle(title);
        poll.setDescription(title);
        poll.setChangeDate(new Date());

        PollOption pollOption1 = new PollOption("Some option no1");
        PollOption pollOption2 = new PollOption("Some option no2");
        List<PollOption> options = new ArrayList<>();
        options.add(pollOption1);
        options.add(pollOption2);

        poll.setPollOptions(options);

        return poll;
    }

    private Poll buildPoll(String title, User author) {
        return buildPoll(null, title, author);
    }

    private User buildNormalUser(String id) {
        return buildUser(false, id);
    }

    private User buildUser(boolean isAdmin, String id) {
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
