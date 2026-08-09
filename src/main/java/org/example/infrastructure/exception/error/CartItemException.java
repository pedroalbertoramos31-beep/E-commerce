package org.example.infrastructure.exception.error;

import org.example.infrastructure.exception.BaseBusinessException;
import org.springframework.http.HttpStatus;

public class CartItemException extends BaseBusinessException {

    private CartItemException(String message, HttpStatus status){
        super(message, status);
    }

    public static class ItemNotFound extends CartItemException {
        public ItemNotFound () {
            super("Product was not found in cart", HttpStatus.NOT_FOUND);
        }
    }

}
