package com.frederic.clienttra.exceptions;

public class CompanyNotFoundException extends RuntimeException {
    public CompanyNotFoundException() {
        super("error.company.not_found");
    }
}
