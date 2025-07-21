package com.frederic.clienttra.dto.create;

import jakarta.validation.ConstraintViolation;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateBankAccountRequestDTOValidationTest extends AbstractValidationTest{

    private CreateBankAccountRequestDTO.CreateBankAccountRequestDTOBuilder baseBuilder(){
        return CreateBankAccountRequestDTO.builder()
                .iban("ES91 2100 0418 4502 0005 1332")
                .swift("ESBBDD")
                .holder("Holder")
                .branch("Prueba");
    }

    @Test
    void validDto_shouldHaveNoViolations(){
        CreateBankAccountRequestDTO dto = baseBuilder().build();

        Set<ConstraintViolation<CreateBankAccountRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    @Test
    void invalidIban_shouldCauseViolation(){
        CreateBankAccountRequestDTO dto = baseBuilder().iban("").build();

        Set<ConstraintViolation<CreateBankAccountRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("iban"));
    }

    @Test
    void shouldReturnLocalizedValidationMessage_whenIbanIsNotValid(){
        LocaleContextHolder.setLocale(new Locale("es"));
        CreateBankAccountRequestDTO dto = baseBuilder().iban("").build();

        Set<ConstraintViolation<CreateBankAccountRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
        String message = violations.iterator().next().getMessage();

        assertThat(message).isEqualTo("El número IBAN no es válido.");
    }

}
