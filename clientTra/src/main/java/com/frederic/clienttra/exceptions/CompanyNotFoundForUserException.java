package com.frederic.clienttra.exceptions;

public class CompanyNotFoundForUserException extends RuntimeException {

    public CompanyNotFoundForUserException(String message) {
        super(message);
    }
    public CompanyNotFoundForUserException() {
        super("error.company.not_found_for_user");
    }
}
