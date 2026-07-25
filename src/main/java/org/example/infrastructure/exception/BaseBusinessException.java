package org.example.infrastructure.exception;

import org.springframework.http.HttpStatus;

public class BaseBusinessException extends RuntimeException{
    final HttpStatus status;

    protected BaseBusinessException(String message, HttpStatus status){
        super(message);
        this.status = status;
    }

}
