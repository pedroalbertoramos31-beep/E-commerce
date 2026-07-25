package org.example.domain.user.dto.response;

import org.example.domain.user.UserRole;
import org.example.domain.user.UserState;

import java.math.BigDecimal;

public record UserEntityResponse(
        Long id,
        String username,
        String password,
        BigDecimal balance,
        UserRole role,
        UserState state
) {
}
