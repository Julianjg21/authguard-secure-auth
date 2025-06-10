package com.Julianj.authguard.service.impl;

import com.Julianj.authguard.dto.*;
import com.Julianj.authguard.entity.*;
import com.Julianj.authguard.repository.*;
import com.Julianj.authguard.security.JwtService;
import com.Julianj.authguard.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // Constructor for dependency injection
    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository, RefreshTokenRepository refreshTokenRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        // Check if email is already registered
        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Error: Email is already in use.");
        }

        // Get the USER role from the database
        Role userRole = roleRepository.findByName("ROLE_USER").orElseThrow(() -> new RuntimeException("Error: Role not found."));

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);

        // Create a new user with encoded password and assigned roles
        User user = new User(request.firstName(), request.lastName(), request.email(), passwordEncoder.encode(request.password()), roles);

        // Save the user entity in the database
        userRepository.save(user);

        // Generate JWT access and refresh tokens
        var accessToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        // Save refresh token in DB for future verification
        saveRefreshToken(user, refreshToken);

        // Return tokens to the client
        return new AuthResponse(accessToken, refreshToken);
    }

    @Override
    public AuthResponse login(@Valid LoginRequest request) {
        try {
            // Try to authenticate the user
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        } catch (AuthenticationException ex) {
            // launches a  exception for email or invalid password
            throw new BadCredentialsException("Incorrect mail or password");
        }

        var user = userRepository.findByEmail(request.email()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Generate tokens
        var accessToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        // Save the refresh token
        saveRefreshToken(user, refreshToken);

        return new AuthResponse(accessToken, refreshToken);
    }


    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String requestRefreshToken = request.token();
        System.out.println(" request refresh token: " + requestRefreshToken);
        // Validate refresh token from the database
        return refreshTokenRepository.findByToken(requestRefreshToken).map(this::verifyExpiration) // Check if token is expired
                .map(RefreshToken::getUser)  // Get associated user
                .map(user -> {
                    // Generate new access token
                    String accessToken = jwtService.generateToken(user);
                    // Return new access token with the same refresh token
                    return new AuthResponse(accessToken, requestRefreshToken);
                }).orElseThrow(() -> new RuntimeException("Refresh token not found in database."));
    }

    // Helper method to check refresh token expiration
    private RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            // Delete expired token from DB
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token has expired. Please log in again.");
        }
        return token;
    }

    // Helper method to save refresh token associated with user
    private void saveRefreshToken(User user, String tokenString) {
        // Delete existing token if already present
        refreshTokenRepository.findByUser_Id(user.getId()).ifPresent(refreshTokenRepository::delete);

        long refreshTokenValidity = 1000L * 60 * 60 * 24 * 7; // 7 days in milliseconds

        // Create new RefreshToken entity with expiry date and user reference
        RefreshToken newRefreshToken = new RefreshToken(tokenString, Instant.now().plusMillis(refreshTokenValidity), user);

        // Save new refresh token to database
        refreshTokenRepository.save(newRefreshToken);
    }
}
