package com.Julianj.authguard.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "The firstname is mandatory.")
        @Size(min = 2, max = 30, message = "The firstname must be between 2 and 30 characters.")
        String firstName,

        @NotBlank(message = "The lastname is mandatory.")
        @Size(min = 2, max = 30, message = "The lastname must be between 2 and 30 characters.")
        String lastName,

        @NotBlank(message = "The email is mandatory.")
        @Email(message = "The email is invalid.")
        String email,

        @NotBlank(message = "The password is mandatory.")
        @Size(min = 8, max = 100, message = "The password must be at least 8 characters long.")
        String password
) {}
