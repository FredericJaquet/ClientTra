package com.frederic.clienttra.exceptions;

public class UserAlreadyExistsException extends RuntimeException{

    public UserAlreadyExistsException(){
        super("error.user.already_exists");
    }
}
