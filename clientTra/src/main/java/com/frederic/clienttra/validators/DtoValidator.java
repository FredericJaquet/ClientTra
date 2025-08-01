package com.frederic.clienttra.validators;

import com.frederic.clienttra.exceptions.ManualValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utility class to validate DTO objects using Jakarta Bean Validation.
 * Throws a ManualValidationException if any constraint violations are found.
 */
@Component
@RequiredArgsConstructor
public class DtoValidator {

    private final Validator validator;

    /**
     * Validates the given DTO instance.
     * If validation errors exist, throws ManualValidationException
     * containing a map of field names and their respective error messages.
     *
     * @param <T> the type of the DTO to validate
     * @param dto the DTO instance to validate
     * @throws ManualValidationException if any validation constraints are violated
     */
    public <T> void validate(T dto) {
        Set<ConstraintViolation<T>> violations = validator.validate(dto);

        if (!violations.isEmpty()) {
            Map<String, String> fieldErrors = violations.stream()
                    .collect(Collectors.toMap(
                            v -> v.getPropertyPath().toString(),
                            ConstraintViolation::getMessage
                    ));

            throw new ManualValidationException(fieldErrors);
        }
    }

}
