package com.frederic.clienttra.exceptions;

/**
 * Exception thrown when a user is not authenticated.
 */
public class UserNotAuthenticatedException extends RuntimeException{
    public UserNotAuthenticatedException()
    {
        super("error.not_authenticated");
    }
}
