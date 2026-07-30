package org.example.domain.fixture.entity;

import org.example.domain.user.User;
import org.example.domain.user.UserRole;
import org.example.domain.user.UserState;

import java.math.BigDecimal;

public class UserFixture {

    public static String DEFAULT_USERNAME = "JohnDoe";
    public static String DEFAULT_PASSWORD = "password123";
    public static BigDecimal DEFAULT_BALANCE = BigDecimal.valueOf(100);
    public static UserRole DEFAULT_ROLE = UserRole.USER;
    public static UserState DEFAULT_STATE = UserState.ACTIVE;

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static class UserBuilder {

        private String username = DEFAULT_USERNAME;
        private String password = DEFAULT_PASSWORD;
        private BigDecimal balance = DEFAULT_BALANCE;
        private UserRole role = DEFAULT_ROLE;
        private UserState state = DEFAULT_STATE;

        public UserBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder balance(BigDecimal balance) {
            this.balance = balance;
            return this;
        }

        public UserBuilder role(UserRole role) {
            this.role = role;
            return this;
        }

        public UserBuilder state(UserState state) {
            this.state = state;
            return this;
        }

        public User build() {

            User user = User.create(username, password);

            user.addBalance(balance);
            user.changeRole(role);
            user.changeState(state);

            return user;
        }
    }

}
