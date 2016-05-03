package com.prodyna.voting.auth.helper;

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

    USER_1("user_1", "Tom", "tom_jones_446", Role.USER),
    USER_2("user_2", "Dirk", "nowitzki_123", Role.USER),
    ADMIN_1("admin", "Tom_Hanks", "tommy", Role.ADMINISTRATOR);

    private String userId;
    private String username;
    private String pass;
    private Role role;

    public User toUserObject() {
        User user = new User();
        user.setUserId(this.userId);
        user.setUserName(this.username);
        user.setPassword(this.pass);
        user.setRole(this.role);

        return user;
    }
}
