package com.frederic.clienttra.dto.create;

import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateUserRequestDTOValidationTest extends AbstractValidationTest {

    private CreateUserRequestDTO.CreateUserRequestDTOBuilder baseBuilder(){
        return CreateUserRequestDTO.builder()
                .username("user")
                .password("userPass")
                .email("user@testing.com")
                .idPlan(1)
                .idRole(1);
    }

    @Test
    void validDTO_ShouldHaveNoViolations(){
        CreateUserRequestDTO dto = baseBuilder().build();

        Set<ConstraintViolation<CreateUserRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    @Test
    void missingUsername_shouldCauseViolation(){
        CreateUserRequestDTO dto = baseBuilder().username("").build();

        Set<ConstraintViolation<CreateUserRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("username"));
    }

    @Test
    void missingPassword_shouldCauseViolation(){
        CreateUserRequestDTO dto = baseBuilder().password("").build();

        Set<ConstraintViolation<CreateUserRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("password"));
    }

    @Test
    void missingRole_shouldCauseViolation(){
        CreateUserRequestDTO dto = baseBuilder().idRole(null).build();

        Set<ConstraintViolation<CreateUserRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("idRole"));
    }

    @Test
    void missingPlan_shouldCauseViolation(){
        CreateUserRequestDTO dto = baseBuilder().idPlan(null).build();

        Set<ConstraintViolation<CreateUserRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("idPlan"));
    }

    @Test
    void invalidEmail_shouldCauseViolation(){
        CreateUserRequestDTO dto = baseBuilder().email("email.com").build();

        Set<ConstraintViolation<CreateUserRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    @Test
    void emailTooLong_shouldCauseViolation(){
        CreateUserRequestDTO dto = baseBuilder().email("emailveryverymuchtoolongforthemaximumautorizedsize@emailveryverymuchtoolongforthemaximumautorizedsize.com").build();

        Set<ConstraintViolation<CreateUserRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    @Test
    void shouldReturnLocalizedValidationMessage_whenUsernameIsBlank() {
        // Simula que el locale activo es "es"
        LocaleContextHolder.setLocale(new Locale("fr"));

        CreateUserRequestDTO dto = baseBuilder().username("").build();

        Set<ConstraintViolation<CreateUserRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
        String message = violations.iterator().next().getMessage();

        assertThat(message).isEqualTo("Le nom d'utilisateur est obligatoire."); // mensaje definido en messages_fr.properties
    }

    @Test
    void shouldReturnLocalizedValidationMessage_whenPasswordIsBlank() {
        // Simula que el locale activo es "es"
        LocaleContextHolder.setLocale(new Locale("fr"));

        CreateUserRequestDTO dto = baseBuilder().password("").build();

        Set<ConstraintViolation<CreateUserRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
        String message = violations.iterator().next().getMessage();

        assertThat(message).isEqualTo("Le mot de passe est obligatoire."); // mensaje definido en messages_fr.properties
    }

    @Test
    void shouldReturnLocalizedValidationMessage_whenIdRoleIsNull() {
        LocaleContextHolder.setLocale(new Locale("es"));

        CreateUserRequestDTO dto = baseBuilder().idRole(null).build();

        Set<ConstraintViolation<CreateUserRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
        String message = violations.iterator().next().getMessage();

        assertThat(message).isEqualTo("El rol est un campo obligatorio."); // mensaje definido en messages_es.properties
    }

    @Test
    void shouldReturnLocalizedValidationMessage_whenIdPlanIsNull() {
        // Simula que el locale activo es "es"
        LocaleContextHolder.setLocale(new Locale("es"));

        CreateUserRequestDTO dto = baseBuilder().idPlan(null).build();

        Set<ConstraintViolation<CreateUserRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
        String message = violations.iterator().next().getMessage();

        assertThat(message).isEqualTo("El plan es un campo obligatorio."); // mensaje definido en messages_es.properties
    }

    @Test
    void shouldReturnLocalizedValidationMessage_whenEmailIsInvalid() {
        // Simula que el locale activo es "es"
        LocaleContextHolder.setLocale(new Locale("es"));

        CreateUserRequestDTO dto = baseBuilder().email("mail.com").build();

        Set<ConstraintViolation<CreateUserRequestDTO>> violations = validator.validate(dto); // javax.validation.Validator (inyectado con @Autowired)

        assertThat(violations).hasSize(1);
        String message = violations.iterator().next().getMessage();

        assertThat(message).isEqualTo("Debes introducir un email válido."); // mensaje definido en messages_es.properties
    }

    @Test
    void shouldReturnLocalizedValidationMessage_whenEmailIsTooLong() {
        // Simula que el locale activo es "es"
        LocaleContextHolder.setLocale(new Locale("es"));

        CreateUserRequestDTO dto = baseBuilder().email("emailveryverymuchtoolongforthemaximumautorizedsize@emailveryverymuchtoolongforthemaximumautorizedsize.com").build();

        Set<ConstraintViolation<CreateUserRequestDTO>> violations = validator.validate(dto); // javax.validation.Validator (inyectado con @Autowired)

        assertThat(violations).hasSize(1);
        String message = violations.iterator().next().getMessage();

        assertThat(message).isEqualTo("El email es demasiado largo (max 100 carácteres)."); // mensaje definido en messages_es.properties
    }

}
