package com.frederic.clienttra.dto.create;

import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateContactPersonRequestDTOValidationTest extends AbstractValidationTest{

    private CreateContactPersonRequestDTO.CreateContactPersonRequestDTOBuilder baseBuilder(){
        return CreateContactPersonRequestDTO.builder()
                .firstname("Firstname")
                .middlename("middlename")
                .lastname("lastname")
                .role("role")
                .email("email@email.com");
    }

    @Test
    void validDto_shouldHaveNoViolations(){
        CreateContactPersonRequestDTO dto = baseBuilder().build();

        Set<ConstraintViolation<CreateContactPersonRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    @Test
    void missingFirstname_shouldCauseViolation(){
        CreateContactPersonRequestDTO dto = baseBuilder().firstname("").build();

        Set<ConstraintViolation<CreateContactPersonRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("firstname"));
    }

    @Test
    void missingMiddlename_shouldCauseViolation(){
        CreateContactPersonRequestDTO dto = baseBuilder().middlename("").build();

        Set<ConstraintViolation<CreateContactPersonRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("middlename"));
    }

    @Test
    void invalidEmail_shouldCauseViolation(){
        CreateContactPersonRequestDTO dto = baseBuilder().email("email.com").build();

        Set<ConstraintViolation<CreateContactPersonRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    @Test
    void emailTooLong_shouldCauseViolation(){
        CreateContactPersonRequestDTO dto = baseBuilder().email("emailveryverymuchtoolongforthemaximumautorizedsize@emailveryverymuchtoolongforthemaximumautorizedsize.com").build();

        Set<ConstraintViolation<CreateContactPersonRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    @Test
    void shouldReturnLocalizedValidationMessage_whenFirstnameIsBlank() {
        // Simula que el locale activo es "es"
        LocaleContextHolder.setLocale(new Locale("es"));

        CreateContactPersonRequestDTO dto = baseBuilder().firstname("").build();

        Set<ConstraintViolation<CreateContactPersonRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
        String message = violations.iterator().next().getMessage();

        assertThat(message).isEqualTo("El nombre es un campo obligatorio."); // mensaje definido en messages_es.properties
    }

    @Test
    void shouldReturnLocalizedValidationMessage_whenMiddlenameIsBlank() {
        // Simula que el locale activo es "es"
        LocaleContextHolder.setLocale(new Locale("es"));

        CreateContactPersonRequestDTO dto = baseBuilder().middlename("").build();

        Set<ConstraintViolation<CreateContactPersonRequestDTO>> violations = validator.validate(dto); // javax.validation.Validator (inyectado con @Autowired)

        assertThat(violations).hasSize(1);
        String message = violations.iterator().next().getMessage();

        assertThat(message).isEqualTo("El primer apellido es un campo obligatorio."); // mensaje definido en messages_es.properties
    }

    @Test
    void shouldReturnLocalizedValidationMessage_whenEmailIsInvalid() {
        // Simula que el locale activo es "es"
        LocaleContextHolder.setLocale(new Locale("es"));

        CreateContactPersonRequestDTO dto = baseBuilder().email("mail.com").build();

        Set<ConstraintViolation<CreateContactPersonRequestDTO>> violations = validator.validate(dto); // javax.validation.Validator (inyectado con @Autowired)

        assertThat(violations).hasSize(1);
        String message = violations.iterator().next().getMessage();

        assertThat(message).isEqualTo("Debes introducir un email válido."); // mensaje definido en messages_es.properties
    }

    @Test
    void shouldReturnLocalizedValidationMessage_whenEmailIsTooLong() {
        // Simula que el locale activo es "es"
        LocaleContextHolder.setLocale(new Locale("es"));

        CreateContactPersonRequestDTO dto = baseBuilder().email("emailveryverymuchtoolongforthemaximumautorizedsize@emailveryverymuchtoolongforthemaximumautorizedsize.com").build();

        Set<ConstraintViolation<CreateContactPersonRequestDTO>> violations = validator.validate(dto); // javax.validation.Validator (inyectado con @Autowired)

        assertThat(violations).hasSize(1);
        String message = violations.iterator().next().getMessage();

        assertThat(message).isEqualTo("El email es demasiado largo (max 100 carácteres)."); // mensaje definido en messages_es.properties
    }

}
