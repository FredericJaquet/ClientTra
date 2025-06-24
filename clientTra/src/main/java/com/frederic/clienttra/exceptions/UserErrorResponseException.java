package com.frederic.clienttra.exceptions;

public class UserErrorResponseException extends RuntimeException{
    public UserErrorResponseException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserErrorResponseException() {

        super("error.resource_not_found");
    }

}
