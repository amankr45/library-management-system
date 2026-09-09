package com.aman.LibraryManagementSystem.dto.request;

import jakarta.validation.constraints.NotBlank;

public class GoogleLoginRequest {

    @NotBlank(message = "Google ID token is required")
    private String token;

    public GoogleLoginRequest() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}