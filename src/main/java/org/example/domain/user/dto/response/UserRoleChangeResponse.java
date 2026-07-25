package org.example.domain.user.dto.response;

import org.example.domain.user.UserRole;

public record UserRoleChangeResponse(
        Long id,
        UserRole role
) {
}
