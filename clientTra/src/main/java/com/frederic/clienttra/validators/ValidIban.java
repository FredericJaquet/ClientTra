package com.frederic.clienttra.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IbanValidatorConstraint.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidIban {
    String message() default "{validation.bank_account.invalid}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
