package org.example.infrastructure.exception.error;

import org.example.infrastructure.exception.BaseBusinessException;
import org.springframework.http.HttpStatus;

public class CartException extends BaseBusinessException {

        private CartException(String message, HttpStatus status){
            super(message, status);
        }

        public static class ItemNotFound extends CartException {
            public ItemNotFound (Long productId) {
                super("Product with ID: " + productId + " does not exists in your cart", HttpStatus.NOT_FOUND);
            }
        }

        public static class CartNotFound extends CartException {
            public CartNotFound () {
                super("Cart does not exists", HttpStatus.NOT_FOUND);
            }
        }

    public static class CartIsEmpty extends CartException {
        public CartIsEmpty () {
            super("You dont have items in your car to pay", HttpStatus.NOT_FOUND);
        }
    }


}
