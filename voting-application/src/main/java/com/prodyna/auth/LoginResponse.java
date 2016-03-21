package com.prodyna.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {

	public LoginResponse() {
	}

	private String token;

	public LoginResponse(final String token) {
		this.token = token;
	}
}
