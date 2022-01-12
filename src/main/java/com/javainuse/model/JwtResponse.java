package com.javainuse.model;

import java.io.Serializable;

public class JwtResponse implements Serializable {

	private static final long serialVersionUID = -8091879091924046844L;
	private final String jwtToken;

	public String getRefreshToken() {
		return refreshToken;
	}

	private final String refreshToken;

	public JwtResponse(String jwtToken, String refreshToken) {
		this.jwtToken = jwtToken;
		this.refreshToken = refreshToken;
	}

	public String getToken() {
		return this.jwtToken;
	}
}