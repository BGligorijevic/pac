package com.prodyna.auth.user;

import java.util.List;

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
    private List<Role> roles;
}
