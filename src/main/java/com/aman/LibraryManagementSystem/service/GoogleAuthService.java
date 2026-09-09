package com.aman.LibraryManagementSystem.service;

import com.aman.LibraryManagementSystem.dto.response.GoogleUserResponse;

public interface GoogleAuthService {

    GoogleUserResponse verifyGoogleToken(String token);
}