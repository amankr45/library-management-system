package com.aman.LibraryManagementSystem.service.impl;

import com.aman.LibraryManagementSystem.dto.response.GoogleUserResponse;
import com.aman.LibraryManagementSystem.service.GoogleAuthService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class GoogleAuthServiceImpl
        implements GoogleAuthService {

    private final GoogleIdTokenVerifier verifier;

    public GoogleAuthServiceImpl(
            @Value("${google.client.id}") String googleClientId
    ) {
        this.verifier =
                new GoogleIdTokenVerifier.Builder(
                        new NetHttpTransport(),
                        GsonFactory.getDefaultInstance()
                )
                        .setAudience(
                                Collections.singletonList(googleClientId)
                        )
                        .build();
    }

    @Override
    public GoogleUserResponse verifyGoogleToken(
            String token
    ) {

        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException(
                    "Google ID token is required."
            );
        }

        try {

            GoogleIdToken idToken =
                    verifier.verify(token);

            if (idToken == null) {
                throw new IllegalArgumentException(
                        "Invalid Google ID token."
                );
            }

            GoogleIdToken.Payload payload =
                    idToken.getPayload();

            String googleId =
                    payload.getSubject();

            String email =
                    payload.getEmail();

            String name =
                    (String) payload.get("name");

            String picture =
                    (String) payload.get("picture");

            if (email == null || email.isBlank()) {
                throw new IllegalArgumentException(
                        "Google ID token does not contain an email address."
                );
            }

            return new GoogleUserResponse(
                    googleId,
                    email,
                    name,
                    picture
            );

        } catch (GeneralSecurityException | IOException exception) {

            throw new IllegalArgumentException(
                    "Failed to verify Google ID token.",
                    exception
            );
        }
    }
}