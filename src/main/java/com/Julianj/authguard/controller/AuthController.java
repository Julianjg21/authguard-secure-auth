package com.Julianj.authguard.controller;

import com.Julianj.authguard.dto.*;
import com.Julianj.authguard.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Handles user registration
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid  @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    // Handles user login
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    // Handles access token refresh
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    // Protected test endpoint
    @GetMapping("/demo")
    public ResponseEntity<String> demo() {
        return ResponseEntity.ok("Hello from a protected endpoint!");
    }
}
