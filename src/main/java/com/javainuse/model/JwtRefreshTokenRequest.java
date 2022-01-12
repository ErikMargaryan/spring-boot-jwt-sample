package com.javainuse.model;


import java.io.Serializable;

public class JwtRefreshTokenRequest implements Serializable {

    private static final long serialVersionUID = 347646574737447L;

    private String refreshToken;

    public JwtRefreshTokenRequest() {
    }

    public JwtRefreshTokenRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
