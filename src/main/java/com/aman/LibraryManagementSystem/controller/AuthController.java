package com.aman.LibraryManagementSystem.controller;

import com.aman.LibraryManagementSystem.dto.request.GoogleLoginRequest;
import com.aman.LibraryManagementSystem.dto.request.LoginRequest;
import com.aman.LibraryManagementSystem.dto.response.AuthResponse;
import com.aman.LibraryManagementSystem.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(
            AuthService authService
    ) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {

        AuthResponse response =
                authService.login(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/google")
    public ResponseEntity<AuthResponse> googleLogin(
            @Valid @RequestBody GoogleLoginRequest request
    ) {

        AuthResponse response =
                authService.googleLogin(request);

        return ResponseEntity.ok(response);
    }
}