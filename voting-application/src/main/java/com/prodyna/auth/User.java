package com.prodyna.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {

	public User() {
	}
	private long id;
	private String userName;
	private String password;
	private Role role;
}
