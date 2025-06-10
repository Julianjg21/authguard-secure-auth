package com.Julianj.authguard.dto;

// Response object containing access and refresh tokens
public record AuthResponse (
        String accessToken,
        String refreshToken
) {
}
