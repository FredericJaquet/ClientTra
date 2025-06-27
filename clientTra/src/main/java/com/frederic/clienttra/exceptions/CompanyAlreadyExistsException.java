package com.frederic.clienttra.exceptions;

public class CompanyAlreadyExistsException extends RuntimeException{
    public CompanyAlreadyExistsException() {
        super("error.company_already_exists");
    }
}
