package com.frederic.clienttra.exceptions;

import lombok.Getter;

import java.util.Map;

/**
 * Exception thrown to represent manual validation errors with specific field error messages.
 * Contains a map of field names to error message keys for detailed validation feedback.
 */
@Getter
public class ManualValidationException extends RuntimeException {
    private final Map<String, String> fieldErrors;

    public ManualValidationException(Map<String, String> fieldErrors) {
        super("validation.manual.failed");
        this.fieldErrors = fieldErrors;
    }

}
