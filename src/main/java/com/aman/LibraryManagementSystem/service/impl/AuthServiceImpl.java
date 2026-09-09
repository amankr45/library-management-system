package com.aman.LibraryManagementSystem.service.impl;

import com.aman.LibraryManagementSystem.dto.request.GoogleLoginRequest;
import com.aman.LibraryManagementSystem.dto.request.LoginRequest;
import com.aman.LibraryManagementSystem.dto.response.AuthResponse;
import com.aman.LibraryManagementSystem.dto.response.GoogleUserResponse;
import com.aman.LibraryManagementSystem.entity.User;
import com.aman.LibraryManagementSystem.enums.Role;
import com.aman.LibraryManagementSystem.repository.UserRepository;
import com.aman.LibraryManagementSystem.security.JwtService;
import com.aman.LibraryManagementSystem.service.AuthService;
import com.aman.LibraryManagementSystem.service.GoogleAuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final GoogleAuthService googleAuthService;
    private final JwtService jwtService;

    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            GoogleAuthService googleAuthService,
            JwtService jwtService
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.googleAuthService = googleAuthService;
        this.jwtService = jwtService;
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );

        UserDetails userDetails =
                (UserDetails) authentication.getPrincipal();

        User user =
                userRepository.findByEmail(userDetails.getUsername())
                        .orElseThrow(
                                () -> new IllegalStateException(
                                        "Authenticated user was not found."
                                )
                        );

        String token = jwtService.generateToken(user);

        return buildAuthResponse(user, token);
    }

    @Override
    @Transactional
    public AuthResponse googleLogin(
            GoogleLoginRequest request
    ) {

        GoogleUserResponse googleUser =
                googleAuthService.verifyGoogleToken(
                        request.getToken()
                );

        User user =
                userRepository.findByEmail(
                        googleUser.getEmail()
                ).orElseGet(
                        () -> createGoogleUser(googleUser)
                );

        if (!user.isEnabled()) {
            throw new IllegalStateException(
                    "User account is disabled."
            );
        }

        String token =
                jwtService.generateToken(user);

        return buildAuthResponse(user, token);
    }

    private User createGoogleUser(
            GoogleUserResponse googleUser
    ) {

        User user = new User(
                googleUser.getName() != null
                        ? googleUser.getName()
                        : googleUser.getEmail(),
                googleUser.getEmail(),
                null,
                Role.LIBRARIAN
        );

        return userRepository.save(user);
    }

    private AuthResponse buildAuthResponse(
            User user,
            String token
    ) {

        AuthResponse.UserResponse userResponse =
                new AuthResponse.UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole()
                );

        return new AuthResponse(
                token,
                userResponse
        );
    }
}