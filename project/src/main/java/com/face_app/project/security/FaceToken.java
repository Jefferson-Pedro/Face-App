package com.face_app.project.security;

public class FaceToken {

    private String token;

    public FaceToken(String token) {
        this.token = token;
    }

    public FaceToken() {}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
