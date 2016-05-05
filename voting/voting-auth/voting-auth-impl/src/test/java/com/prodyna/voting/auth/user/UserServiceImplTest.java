package com.prodyna.voting.auth.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

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
     * Test for {@link UserServiceImpl#findUserByUserNameAndPassword(String, String)}.
     */
    @Test
    public void findsUserByUserNameAndPassword() {
        User user = new User();
        user.setUserId("12345");

        when(userRepository.findByUserNameAndPassword(anyString(), anyString())).thenReturn(user);
        Optional<User> foundUser = userService.findUserByUserNameAndPassword("bla", "bla");
        assertTrue(foundUser.isPresent());
        assertTrue(foundUser.get().getUserId().equals(user.getUserId()));
    }

    /**
     * Test for {@link UserServiceImpl#findUserByUserNameAndPassword(String, String)}.
     */
    @Test
    public void findsUserByUserNameAndPasswordReturnsEmptyOptional() {
        when(userRepository.findByUserNameAndPassword(anyString(), anyString())).thenReturn(null);
        Optional<User> foundUser = userService.findUserByUserNameAndPassword("bla", "bla");
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
