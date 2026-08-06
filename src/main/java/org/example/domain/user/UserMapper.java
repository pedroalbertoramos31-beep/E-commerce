package org.example.domain.user;

import org.example.domain.user.dto.response.UserBalanceResponse;
import org.example.domain.user.dto.response.UserProfileResponse;
import org.example.domain.user.dto.response.UserRoleChangeResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserProfileResponse toProfileResponse(User user);

    UserBalanceResponse toBalanceResponse(User user);

    UserRoleChangeResponse toUserRoleChangeResponse(User user);

}
