package com.aman.LibraryManagementSystem.config;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

import com.aman.LibraryManagementSystem.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String jwtSecret;

    public SecurityConfig(
            CustomUserDetailsService userDetailsService
    ) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecretKey jwtSecretKey() {

        byte[] decodedSecret =
                Base64.getDecoder().decode(jwtSecret);

        return new SecretKeySpec(
                decodedSecret,
                "HmacSHA256"
        );
    }

    @Bean
    public JwtEncoder jwtEncoder(
            SecretKey jwtSecretKey
    ) {
        return NimbusJwtEncoder.withSecretKey(
                jwtSecretKey
        ).build();
    }

    @Bean
    public JwtDecoder jwtDecoder(
            SecretKey jwtSecretKey
    ) {
        return NimbusJwtDecoder
                .withSecretKey(jwtSecretKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(
                        userDetailsService
                );

        provider.setPasswordEncoder(
                passwordEncoder()
        );

        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {

        return new org.springframework.security.authentication.ProviderManager(
                authenticationProvider()
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/api/auth/**"
                        ).permitAll()

                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**"
                        ).permitAll()

                        .anyRequest().authenticated()
                )

                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt -> {})
                );

        return http.build();
    }
}