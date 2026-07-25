package org.example.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRegisterRequest(
        @NotBlank(message = "Username cannot be blank")
        @Size(min = 3, max = 20, message = "Username needs to have at least 3 characters and max 20")
        String username,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 8, message = "Password needs to have at least 8 characters")
        String password
) {}
