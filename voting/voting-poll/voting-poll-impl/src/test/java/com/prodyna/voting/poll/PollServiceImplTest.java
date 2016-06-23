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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Unit tests for class {@link PollServiceImpl}.
 */
@RunWith(PowerMockRunner.class)
public class PollServiceImplTest {

    private static final String USER_ADMIN = "admin";
    private static final String USER_1 = "user1";
    private static final String USER_2 = "user2";
    private static final String POLL_ID = UUID.randomUUID().toString();

    @InjectMocks
    private PollServiceImpl pollService;

    @Mock
    private PollRepository pollRepository;

    /**
     * Test for {@link PollServiceImpl#getAllPolls()}.
     */
    @Test
    public void getAllPollsReturnsCorrectNumberOfVotes() {
        User normalUser = buildNormalUser(USER_1);
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
     * Test for {@link PollServiceImpl#getPoll(String)}.
     */
    @Test
    public void getPollReturnsPoll() {
        Poll poll = buildPoll(POLL_ID, "Your favourite OS?", buildNormalUser(USER_1));

        when(pollRepository.findOne(POLL_ID)).thenReturn(poll);

        Optional<Poll> voteResult = pollService.getPoll(POLL_ID);
        assertTrue(voteResult.isPresent());
    }

    /**
     * Test for {@link PollServiceImpl#getPoll(String)}.
     */
    @Test
    public void getPollReturnsAbsent() {
        when(pollRepository.findOne(POLL_ID)).thenReturn(null);
        Optional<Poll> voteResult = pollService.getPoll(POLL_ID);
        assertFalse(voteResult.isPresent());
    }

    /**
     * Test for {@link PollServiceImpl#deletePoll(String, User)}.
     */
    @Test
    public void pollIsDeletedWhenPollIsHis() {
        User normalUser = buildNormalUser(USER_1);
        Poll poll = buildPoll(POLL_ID, "Your favourite OS?", buildNormalUser(USER_1));

        when(pollRepository.findOne(POLL_ID)).thenReturn(poll);

        pollService.deletePoll(poll.get_id(), normalUser);

        verify(pollRepository).delete(poll);
    }

    /**
     * Test for {@link PollServiceImpl#deletePoll(String, User)}.
     */
    @Test
    public void pollIsDeletedAlwaysWhenUserIsAdmin() {
        Poll poll = buildPoll(POLL_ID, "Your favourite OS?", buildNormalUser(USER_2));

        when(pollRepository.findOne(POLL_ID)).thenReturn(poll);
        pollService.deletePoll(poll.get_id(), buildUser(true, USER_ADMIN));

        verify(pollRepository).delete(poll);
    }

    /**
     * Test for {@link PollServiceImpl#deletePoll(String, User)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void pollIsNotDeletedWhenUserIsNormalUserAndPollIsNotHis() {
        Poll poll = buildPoll(POLL_ID, "Your favourite OS?", buildNormalUser(USER_1));

        when(pollRepository.findOne(POLL_ID)).thenReturn(poll);
        pollService.deletePoll(poll.get_id(), buildNormalUser(USER_2));
    }

    /**
     * Test for {@link PollServiceImpl#deletePoll(String, User)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void pollIsNotDeletedWhenPollIsNotFoundById() {
        when(pollRepository.findOne(POLL_ID)).thenReturn(null);
        pollService.deletePoll(POLL_ID, buildNormalUser(USER_1));
    }

    /**
     * Test for {@link PollServiceImpl#createPoll(Poll,User)}.
     */
    @Test
    public void pollIsCreated() {
        User user = buildNormalUser(USER_1);
        Poll poll = buildPoll("Which is your favourite color?", user);
        assertTrue(poll.get_id() == null);

        when(pollRepository.insert(poll)).thenReturn(poll);

        pollService.createPoll(poll, user);
        verify(pollRepository).insert(poll);
    }

    /**
     * Test for {@link PollServiceImpl#createPoll(Poll,User)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void pollIsNotCreatedForNullInput() {
        pollService.createPoll(null, null);
    }

    /**
     * Test for {@link PollServiceImpl#editPoll(Poll, User)}.
     */
    @Test
    public void pollIsEditedIfAdmin() {
        Poll poll = buildPoll("123456", "Which is your favourite color?", buildNormalUser(USER_1));

        when(pollRepository.findOne(poll.get_id())).thenReturn(poll);
        when(pollRepository.save(poll)).thenReturn(poll);

        pollService.editPoll(poll, buildUser(true, USER_ADMIN));

        verify(pollRepository).save(poll);
    }

    /**
     * Test for {@link PollServiceImpl#editPoll(Poll, User)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void pollCannotBeEditedIfNotAdminAndNotHisOwnPoll() {
        Poll poll = buildPoll("123456", "Which is your favourite color?", buildNormalUser(USER_1));

        pollService.editPoll(poll, buildNormalUser(USER_2));
    }

    /**
     * Test for {@link PollServiceImpl#vote(String, String, User)}.
     */
    @Test
    public void votingWorks() {
        User user = buildNormalUser(USER_1);
        Poll poll = buildPoll("123456", "Which is your favourite color?", buildNormalUser(USER_1));
        when(pollRepository.findOne(poll.get_id())).thenReturn(poll);
        when(pollRepository.save(poll)).thenReturn(poll);

        List<Vote> votesBeforeVoting = new ArrayList<>();
        for (PollOption pollOption : poll.getPollOptions()) {
            votesBeforeVoting.addAll(pollOption.getVotes());
        }
        assertTrue(votesBeforeVoting.size() == 0);

        Poll pollWithVotes = pollService.vote(poll.get_id(), poll.getPollOptions().get(0).get_id(), user);

        List<Vote> votesAfterVoting = new ArrayList<>();
        for (PollOption pollOption : pollWithVotes.getPollOptions()) {
            votesAfterVoting.addAll(pollOption.getVotes());
        }
        assertTrue(votesAfterVoting.size() == 1);
        verify(pollRepository, times(1)).save(pollWithVotes);
    }

    private Poll buildPoll(String id, String title, User author) {
        Poll poll = new Poll();

        poll.setCreator(author.getUserName());
        poll.set_id(id);
        poll.setTitle(title);
        poll.setDescription(title);
        poll.setChangeDate(new Date());

        PollOption pollOption1 = new PollOption("Some option no1", poll.get_id());
        PollOption pollOption2 = new PollOption("Some option no2", poll.get_id());
        List<PollOption> options = new ArrayList<>();
        options.add(pollOption1);
        options.add(pollOption2);

        poll.setPollOptions(options);

        return poll;
    }

    private Poll buildPoll(String title, User author) {
        return buildPoll(null, title, author);
    }

    private User buildNormalUser(String userName) {
        return buildUser(false, userName);
    }

    private User buildUser(boolean isAdmin, String userName) {
        User user = new User();

        if (isAdmin) {
            user.setUserName("admin");
            user.setPassword("_admin");
            user.setRole(Role.ADMINISTRATOR);
        } else {
            user.setUserName(userName);
            user.setPassword("_user");
            user.setRole(Role.USER);
        }

        return user;
    }
}
