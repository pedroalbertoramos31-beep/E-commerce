package org.example.domain.user;

import org.example.domain.user.dto.response.UserBalanceResponse;
import org.example.domain.user.dto.response.UserEntityResponse;
import org.example.domain.user.dto.response.UserProfileResponse;
import org.example.domain.user.dto.response.UserRoleChangeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserEntityResponse toUserResponse(User user);

    UserProfileResponse toProfileResponse(User user);

    @Mapping(source = "user", target = "user")
    UserBalanceResponse toBalanceResponse(User user);

    UserRoleChangeResponse toUserRoleChangeResponse(User user);

}
