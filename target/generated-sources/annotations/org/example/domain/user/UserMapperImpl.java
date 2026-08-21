package org.example.domain.user;

import java.math.BigDecimal;
import javax.annotation.processing.Generated;
import org.example.domain.user.dto.response.UserBalanceResponse;
import org.example.domain.user.dto.response.UserProfileResponse;
import org.example.domain.user.dto.response.UserRoleChangeResponse;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-21T12:25:35-0700",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.12 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserProfileResponse toProfileResponse(User user) {
        if ( user == null ) {
            return null;
        }

        Long id = null;
        String username = null;
        UserRole role = null;

        id = user.getId();
        username = user.getUsername();
        role = user.getRole();

        UserProfileResponse userProfileResponse = new UserProfileResponse( id, username, role );

        return userProfileResponse;
    }

    @Override
    public UserBalanceResponse toBalanceResponse(User user) {
        if ( user == null ) {
            return null;
        }

        BigDecimal balance = null;

        balance = user.getBalance();

        UserBalanceResponse userBalanceResponse = new UserBalanceResponse( balance );

        return userBalanceResponse;
    }

    @Override
    public UserRoleChangeResponse toUserRoleChangeResponse(User user) {
        if ( user == null ) {
            return null;
        }

        Long id = null;
        UserRole role = null;

        id = user.getId();
        role = user.getRole();

        UserRoleChangeResponse userRoleChangeResponse = new UserRoleChangeResponse( id, role );

        return userRoleChangeResponse;
    }
}
