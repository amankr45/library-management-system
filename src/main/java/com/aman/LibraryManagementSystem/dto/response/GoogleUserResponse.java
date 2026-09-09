package com.aman.LibraryManagementSystem.dto.response;

public class GoogleUserResponse {

    private String googleId;
    private String email;
    private String name;
    private String picture;

    public GoogleUserResponse() {
    }

    public GoogleUserResponse(
            String googleId,
            String email,
            String name,
            String picture
    ) {
        this.googleId = googleId;
        this.email = email;
        this.name = name;
        this.picture = picture;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}