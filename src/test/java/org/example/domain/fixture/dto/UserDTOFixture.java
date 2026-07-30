package org.example.domain.fixture.dto;

import org.example.domain.fixture.entity.UserFixture;
import org.example.domain.user.dto.request.UserAddBalanceRequest;
import org.example.domain.user.dto.request.UserRegisterRequest;

import java.math.BigDecimal;

public class UserDTOFixture {

    public static UserRegisterRequest userRegisterRequest(){
        return new UserRegisterRequest(
                UserFixture.DEFAULT_USERNAME,
                UserFixture.DEFAULT_PASSWORD
        );
    }

    public static UserAddBalanceRequest userAddBalanceRequest(){
        return new UserAddBalanceRequest(
                BigDecimal.valueOf(100)
        );
    }



}
