package com.frederic.clienttra.dto.create;

import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class CreatePhoneRequestDTOValidationTest extends AbstractValidationTest{

    private CreatePhoneRequestDTO.CreatePhoneRequestDTOBuilder baseBuilder(){
        return CreatePhoneRequestDTO.builder()
                .phoneNumber("123456789")
                .kind("móvil");
    }

    @Test
    void validDto_shouldHaveNoViolations(){
        CreatePhoneRequestDTO dto = baseBuilder().build();

        Set<ConstraintViolation<CreatePhoneRequestDTO>> violations = validator.validate((dto));
        assertThat(violations).isEmpty();
    }

    @Test
    void missingPhoneNumber_shouldCauseViolation(){
        CreatePhoneRequestDTO dto = baseBuilder().phoneNumber("").build();

        Set<ConstraintViolation<CreatePhoneRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("phoneNumber"));
    }

    @Test
    void shouldReturnLocalizedValidationMessage_whePhoneNumberIsBlank(){
        LocaleContextHolder.setLocale(new Locale("es"));

        CreatePhoneRequestDTO dto = baseBuilder().phoneNumber("").build();

        Set<ConstraintViolation<CreatePhoneRequestDTO>> violations = validator.validate((dto));

        assertThat(violations).hasSize(1);
        String message = violations.iterator().next().getMessage();

        assertThat(message).isEqualTo("El número de teléfono es un campo obligatorio.");
    }

}
