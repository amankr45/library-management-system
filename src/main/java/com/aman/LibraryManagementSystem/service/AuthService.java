package com.aman.LibraryManagementSystem.service;

import com.aman.LibraryManagementSystem.dto.request.GoogleLoginRequest;
import com.aman.LibraryManagementSystem.dto.request.LoginRequest;
import com.aman.LibraryManagementSystem.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse login(LoginRequest request);

    AuthResponse googleLogin(GoogleLoginRequest request);
}