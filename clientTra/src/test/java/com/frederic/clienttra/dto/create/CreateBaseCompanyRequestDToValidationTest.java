package com.frederic.clienttra.dto.create;

import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateBaseCompanyRequestDToValidationTest extends AbstractValidationTest{

    private CreateBaseCompanyRequestDTO.CreateBaseCompanyRequestDTOBuilder baseBuilder(){
        return CreateBaseCompanyRequestDTO.builder()
                .comName("comName")
                .legalName("legalName")
                .vatNumber("vatNumber")
                .email("email@testing.com")
                .addresses(new ArrayList<CreateAddressRequestDTO>());
    }

    @Test
    void validDto_shouldHaveNoViolations(){
        CreateBaseCompanyRequestDTO dto = baseBuilder().build();

        Set<ConstraintViolation<CreateBaseCompanyRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    @Test
    void missingLegalName_shouldCauseViolation(){
        CreateBaseCompanyRequestDTO dto = baseBuilder().legalName("").build();

        Set<ConstraintViolation<CreateBaseCompanyRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("legalName"));
    }

    @Test
    void missingVatNumber_shouldCauseViolation(){
        CreateBaseCompanyRequestDTO dto = baseBuilder().vatNumber("").build();

        Set<ConstraintViolation<CreateBaseCompanyRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("vatNumber"));
    }

    @Test
    void invalidEmail_shouldCauseViolation(){
        CreateBaseCompanyRequestDTO dto = baseBuilder().email("email.com").build();

        Set<ConstraintViolation<CreateBaseCompanyRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    @Test
    void emailTooLong_shouldCauseViolation(){
        CreateBaseCompanyRequestDTO dto = baseBuilder().email("emailveryverymuchtoolongforthemaximumautorizedsize@emailveryverymuchtoolongforthemaximumautorizedsize.com").build();

        Set<ConstraintViolation<CreateBaseCompanyRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    @Test
    void addressesNull_shouldCauseViolation(){
        CreateBaseCompanyRequestDTO dto = baseBuilder().build();
        dto.setAddresses(null);

        Set<ConstraintViolation<CreateBaseCompanyRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("addresses"));
    }

    @Test
    void shouldReturnLocalizedValidationMessage_whenLegalNameIsBlank() {
        // Simula que el locale activo es "es"
        LocaleContextHolder.setLocale(new Locale("es"));

        CreateBaseCompanyRequestDTO dto = baseBuilder().legalName("").build();

        Set<ConstraintViolation<CreateBaseCompanyRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
        String message = violations.iterator().next().getMessage();

        assertThat(message).isEqualTo("El nombre fiscal es un campo obligatorio."); // mensaje definido en messages_es.properties
    }

    @Test
    void shouldReturnLocalizedValidationMessage_whenVatNumberIsBlank() {
        // Simula que el locale activo es "es"
        LocaleContextHolder.setLocale(new Locale("es"));

        CreateBaseCompanyRequestDTO dto = baseBuilder().vatNumber("").build();

        Set<ConstraintViolation<CreateBaseCompanyRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
        String message = violations.iterator().next().getMessage();

        assertThat(message).isEqualTo("El CIF es un campo obligatorio. Si la empresa no tiene CIF, indica \"N/A\"."); // mensaje definido en messages_es.properties
    }

    @Test
    void shouldReturnLocalizedValidationMessage_whenEmailIsInvalid() {
        // Simula que el locale activo es "es"
        LocaleContextHolder.setLocale(new Locale("es"));

        CreateBaseCompanyRequestDTO dto = baseBuilder().email("mail.com").build();

        Set<ConstraintViolation<CreateBaseCompanyRequestDTO>> violations = validator.validate(dto); // javax.validation.Validator (inyectado con @Autowired)

        assertThat(violations).hasSize(1);
        String message = violations.iterator().next().getMessage();

        assertThat(message).isEqualTo("Debes introducir un email válido."); // mensaje definido en messages_es.properties
    }

    @Test
    void shouldReturnLocalizedValidationMessage_whenEmailIsTooLong() {
        // Simula que el locale activo es "es"
        LocaleContextHolder.setLocale(new Locale("es"));

        CreateBaseCompanyRequestDTO dto = baseBuilder().email("emailveryverymuchtoolongforthemaximumautorizedsize@emailveryverymuchtoolongforthemaximumautorizedsize.com").build();

        Set<ConstraintViolation<CreateBaseCompanyRequestDTO>> violations = validator.validate(dto); // javax.validation.Validator (inyectado con @Autowired)

        assertThat(violations).hasSize(1);
        String message = violations.iterator().next().getMessage();

        assertThat(message).isEqualTo("El email es demasiado largo (max 100 carácteres)."); // mensaje definido en messages_es.properties
    }

    @Test
    void shouldReturnLocalizedValidationMessage_whenAddressesIsNull(){
        LocaleContextHolder.setLocale(new Locale("fr"));

        CreateBaseCompanyRequestDTO dto = baseBuilder().build();
        dto.setAddresses(null);
        Set<ConstraintViolation<CreateBaseCompanyRequestDTO>> violations = validator.validate(dto); // javax.validation.Validator (inyectado con @Autowired)

        assertThat(violations).hasSize(1);
        String message = violations.iterator().next().getMessage();

        assertThat(message).isEqualTo("Il est obligatoire d'indiquer au moins une adresse.");
    }

}
