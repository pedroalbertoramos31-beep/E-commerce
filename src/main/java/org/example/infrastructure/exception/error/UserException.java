package org.example.infrastructure.exception.error;

import org.example.infrastructure.exception.BaseBusinessException;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

public class UserException extends BaseBusinessException {

    private UserException(String message, HttpStatus status){
        super(message, status);
    }

    public static class UserNotFound extends UserException {
        public UserNotFound(Long userId) {
            super("User with ID: " + userId + " does not exists", HttpStatus.NOT_FOUND);
        }
    }

    public static class DuplicateUsername extends UserException {
        public DuplicateUsername (String username) {
            super("A user with: " + username + " already exists", HttpStatus.CONFLICT);
        }
    }

    public static class InsufficientBalance extends UserException {

        public InsufficientBalance(BigDecimal required, BigDecimal available) {
            super("Required balance: " + required + ", but available was: " + available, HttpStatus.BAD_REQUEST);
        }
    }

    public static class AdminRoleModification extends UserException {

        public AdminRoleModification() {
            super("You can not modify the role of an user with role admin", HttpStatus.FORBIDDEN);
        }
    }


}
