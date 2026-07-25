package org.example.domain.factory;

import org.example.domain.user.User;
import org.example.domain.user.UserRole;
import org.example.domain.user.UserState;
import org.example.domain.user.dto.request.UserAddBalanceRequest;
import org.example.domain.user.dto.request.UserRegisterRequest;

import java.math.BigDecimal;

public class UserTestData {

    public static String DEFAULT_USERNAME = "JohnDoe";
    public static String DEFAULT_PASSWORD = "password123";
    public static BigDecimal DEFAULT_BALANCE = BigDecimal.valueOf(100);
    public static UserRole DEFAULT_ROLE = UserRole.USER;
    public static UserState DEFAULT_STATE = UserState.ACTIVE;


    public static User simpleUser(){

        User user = User.create(
                DEFAULT_USERNAME,
                DEFAULT_PASSWORD
        );

        user.addBalance(DEFAULT_BALANCE);
        user.changeRole(DEFAULT_ROLE);
        user.changeState(DEFAULT_STATE);

        return user;
    }

    public static User simpleUser(String username){

        User user = User.create(
                username,
                DEFAULT_PASSWORD
        );

        user.addBalance(DEFAULT_BALANCE);
        user.changeRole(DEFAULT_ROLE);
        user.changeState(DEFAULT_STATE);

        return user;
    }

    public static User simpleUser(UserRole role){

        User user = User.create(
                DEFAULT_USERNAME,
                DEFAULT_PASSWORD
        );

        user.addBalance(DEFAULT_BALANCE);
        user.changeRole(role);
        user.changeState(DEFAULT_STATE);

        return user;
    }

    public static UserRegisterRequest userRegisterRequest(){
        return new UserRegisterRequest(
                DEFAULT_USERNAME,
                DEFAULT_PASSWORD
        );
    }

    public static UserAddBalanceRequest userAddBalanceRequest(){
        return new UserAddBalanceRequest(
                BigDecimal.valueOf(100)
        );
    }

}
