package com.frederic.clienttra.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Custom validation annotation to validate that a string is a valid IBAN.
 * Can be applied to fields and method parameters.
 */
@Documented
@Constraint(validatedBy = IbanValidatorConstraint.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidIban {

    /**
     * The default error message if the IBAN is invalid.
     */
    String message() default "{validation.bank_account.invalid}";

    /**
     * Allows specification of validation groups.
     */
    Class<?>[] groups() default {};

    /**
     * Can be used by clients to assign custom payload objects to a constraint.
     */
    Class<? extends Payload>[] payload() default {};
}
