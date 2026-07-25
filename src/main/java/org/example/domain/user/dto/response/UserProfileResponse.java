package org.example.domain.user.dto.response;

import org.example.domain.user.UserRole;

public record UserProfileResponse(

        Long id,
        String username,
        UserRole role) {

}



