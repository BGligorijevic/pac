package com.prodyna.voting.auth.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class User {

    private String email;
    private String userName;
    private String password;
    private Role role;
}
