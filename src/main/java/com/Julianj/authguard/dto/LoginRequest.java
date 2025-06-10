package com.Julianj.authguard.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// Request object for user login
public record LoginRequest(
        @NotBlank(message = "Email is required.")
        @Email(message = "Email format is invalid.")
        String email,

        @NotBlank(message = "Password is required.")
        String password
) {}