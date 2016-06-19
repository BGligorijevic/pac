package com.prodyna.voting.auth.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    /**
     * Test for {@link UserServiceImpl#saveUser(User)}.
     */
    @Test
    public void savesUserCorrectly() {
        User user = createUser();
        when(userRepository.save(user)).thenReturn(user);

        userService.saveUser(user);

        verify(userRepository, times(1)).save(user);
    }

    /**
     * Test for {@link UserServiceImpl#deleteAllUsers(User)}.
     */
    @Test
    public void deletesAllUsers() {
        userService.deleteAllUsers(createAdminUser());
        verify(userRepository).deleteAll();
    }

    /**
     * Test for {@link UserServiceImpl#deleteAllUsers(User)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void deniesDeleteAllUsers() {
        userService.deleteAllUsers(createUser());
    }

    /**
     * Test for {@link UserServiceImpl#findUserByUserName(String)}.
     */
    @Test
    public void findsUserByUserName() {
        User user = new User();
        user.setUserName("12345");

        when(userRepository.findByUserName(anyString())).thenReturn(user);
        Optional<User> foundUser = userService.findUserByUserName("bla");
        assertTrue(foundUser.isPresent());
        assertTrue(foundUser.get().getUserName().equals(user.getUserName()));
    }

    /**
     * Test for {@link UserServiceImpl#findUserByUserName(String)}.
     */
    @Test
    public void findsUserByUserNameReturnsEmptyOptional() {
        when(userRepository.findByUserName(anyString())).thenReturn(null);
        Optional<User> foundUser = userService.findUserByUserName("bla");
        assertTrue(!foundUser.isPresent());
    }

    private User createAdminUser() {
        User user = new User();

        user.setUserName("admin");
        user.setPassword("_admin");
        user.setRole(Role.ADMINISTRATOR);

        return user;
    }

    private User createUser() {
        User user = new User();

        user.setUserName("user");
        user.setPassword("_user");
        user.setRole(Role.USER);

        return user;
    }
}
