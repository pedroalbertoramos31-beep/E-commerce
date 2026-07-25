package org.example.infrastructure.exception.error;

import org.example.infrastructure.exception.BaseBusinessException;
import org.springframework.http.HttpStatus;

public class ProductException extends BaseBusinessException {

    private ProductException(String message, HttpStatus status){
        super(message, status);
    }

    public static class NotFound extends ProductException {
        public NotFound (Long productId) {
            super("Product with ID: " + productId + " does not exists", HttpStatus.NOT_FOUND);
        }
    }

    public static class DuplicateName extends ProductException {
        public DuplicateName (String name) {
            super("A product with name: " + name + " already exists", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static class InvalidQuantity extends ProductException {
        public InvalidQuantity () {
            super("Quantity must be at least 1", HttpStatus.BAD_REQUEST);
        }
    }

    public static class IllegalResourceAccess extends ProductException {
        public IllegalResourceAccess() {
            super("You are not the owner of this product" ,HttpStatus.FORBIDDEN);
        }
    }

    public static class InsufficientStock extends ProductException {
        public InsufficientStock() {
            super("We don't have enough stock to sell you" ,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static class InvalidState extends ProductException {
        public InvalidState () {
            super("Only products waiting for approval can be approved", HttpStatus.BAD_REQUEST);
        }
    }
}