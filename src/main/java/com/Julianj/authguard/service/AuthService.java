package com.Julianj.authguard.service;

import com.Julianj.authguard.dto.*;

public interface AuthService {

    //Registers a new user with the given registration details.
    AuthResponse register(RegisterRequest request);

    //Authenticates a user using their login credentials.
    AuthResponse login(LoginRequest request);

    //Generates a new access token using a valid refresh token.
    AuthResponse refreshToken(RefreshTokenRequest request);
}
