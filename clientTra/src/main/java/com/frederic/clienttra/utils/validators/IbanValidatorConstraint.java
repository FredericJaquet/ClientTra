package com.frederic.clienttra.utils.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IbanValidatorConstraint implements ConstraintValidator<ValidIban, String> {

    @Override
    public boolean isValid(String iban, ConstraintValidatorContext context) {
        if (iban == null || iban.isBlank()) {
            return false;
        }
        return IbanValidator.isValidIban(iban);
    }
}