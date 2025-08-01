package com.frederic.clienttra.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator implementation for the {@link ValidIban} annotation.
 * Validates that a given string is a valid IBAN using the {@link IbanValidator} utility.
 */
public class IbanValidatorConstraint implements ConstraintValidator<ValidIban, String> {

    /**
     * Checks whether the provided IBAN string is valid.
     *
     * @param iban the IBAN string to validate
     * @param context the context in which the constraint is evaluated
     * @return {@code true} if the IBAN is valid; {@code false} otherwise
     */
    @Override
    public boolean isValid(String iban, ConstraintValidatorContext context) {
        if (iban == null || iban.isBlank()) {
            return false;
        }
        return IbanValidator.isValidIban(iban);
    }
}
