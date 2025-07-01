package com.frederic.clienttra.utils.validators;

import com.frederic.clienttra.exceptions.ManualValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DtoValidator {

    private final Validator validator;

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
