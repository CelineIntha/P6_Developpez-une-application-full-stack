package com.openclassrooms.mddapi.responses;

public class UpdateUserResponse {
    private String token;

    public UpdateUserResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}