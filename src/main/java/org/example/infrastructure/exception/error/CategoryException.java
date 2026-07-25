package org.example.infrastructure.exception.error;

import org.example.infrastructure.exception.BaseBusinessException;
import org.springframework.http.HttpStatus;

public class CategoryException extends BaseBusinessException {

    private CategoryException(String message, HttpStatus status) {
        super(message, status);
    }

    public static class DuplicateName extends CategoryException {
        public DuplicateName(String name) {
            super("Category with name: " + name + " already exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static class NotFound extends CategoryException {
        public NotFound() {
            super("One or more categories ID are invalid or do not exists", HttpStatus.NOT_FOUND);
        }
    }

}