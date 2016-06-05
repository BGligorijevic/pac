package com.prodyna.voting.auth.helper;

import com.prodyna.voting.Application;
import com.prodyna.voting.auth.user.UserService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Meant to be used when testing UI.
 * All tests will be deliberately set on ignore.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class TestUserGenerator {

    @Autowired
    private UserService userService;

    @Test
    @Ignore
    public void generateSampleTestingUsers() {
        userService.saveUser(TestUser.USER_1.toUserObject());
        System.out.println("********* Created user for testing purposes (copy the values) *********");
        System.out.println("UserName: " + TestUser.USER_1.getUsername());
        System.out.println("Password: " + TestUser.USER_1.getPass());
    }
}
