package com.prodyna.voting.sampledata;

import com.prodyna.voting.auth.user.Role;
import com.prodyna.voting.auth.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Sample test users data.
 */
@Getter
@AllArgsConstructor
public enum TestUser {

    USER_1("test", "test", Role.USER),
    USER_2("test2", "test2", Role.USER),
    ADMIN_1("admin", "admin", Role.ADMINISTRATOR);

    private String username;
    private String pass;
    private Role role;

    public User toUserObject() {
        User user = new User();
        user.setUserName(this.username);
        user.setPassword(this.pass);
        user.setRole(this.role);

        return user;
    }

    public static final TestUser[] ALL_USERS = new TestUser[]{USER_1, USER_2, ADMIN_1};
}
