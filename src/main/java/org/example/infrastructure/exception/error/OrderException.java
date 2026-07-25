package org.example.infrastructure.exception.error;

import org.example.infrastructure.exception.BaseBusinessException;
import org.springframework.http.HttpStatus;

public class OrderException extends BaseBusinessException {

    private OrderException(String message, HttpStatus status){
        super(message, status);
    }

    public static class OrderNotFound extends OrderException {
        public OrderNotFound () {
            super("You dont have orders with that ID", HttpStatus.NOT_FOUND);
        }
    }

}
