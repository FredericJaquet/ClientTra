package com.frederic.clienttra.exceptions;

import lombok.Getter;

import java.util.Map;

@Getter
public class ManualValidationException extends RuntimeException {
    private final Map<String, String> fieldErrors;

    public ManualValidationException(Map<String, String> fieldErrors) {
        super("manual.validation.failed");
        this.fieldErrors = fieldErrors;
    }

}
