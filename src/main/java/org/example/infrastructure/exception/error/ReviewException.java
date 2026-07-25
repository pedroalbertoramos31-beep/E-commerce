package org.example.infrastructure.exception.error;

import org.example.infrastructure.exception.BaseBusinessException;
import org.springframework.http.HttpStatus;

public class ReviewException extends BaseBusinessException {

    private ReviewException(String message, HttpStatus status) {
        super(message, status);
    }

    public static class ProductNotBought extends ReviewException {
        public ProductNotBought() {
            super("You can not review a not bought product", HttpStatus.BAD_REQUEST);
        }
    }

    public static class AlreadyReviewed extends ReviewException {
        public AlreadyReviewed() {
            super("You have already reviewed this product", HttpStatus.BAD_REQUEST);
        }
    }

    public static class SelfReview extends ReviewException {
        public SelfReview() {
            super("You cannot review your own products", HttpStatus.BAD_REQUEST);
        }
    }

    public static class ReviewNotFound extends ReviewException {
        public ReviewNotFound() {
            super("Review does not exists", HttpStatus.NOT_FOUND);
        }
    }

}