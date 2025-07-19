package com.frederic.clienttra.dto.create;

import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class CreateAddressRequestDTOValidationTest extends AbstractValidationTest {

    private CreateAddressRequestDTO.CreateAddressRequestDTOBuilder baseBuilder() {
        return CreateAddressRequestDTO.builder()
                .street("Calle Falsa")
                .stNumber("123")
                .apt("2A")
                .cp("28001")
                .city("Madrid")
                .state("Madrid")
                .country("España");
    }

    @Test
    void validDto_shouldHaveNoViolations() {
        CreateAddressRequestDTO dto = baseBuilder().build();

        Set<ConstraintViolation<CreateAddressRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    @Test
    void missingStreet_shouldCauseViolation() {
        CreateAddressRequestDTO dto = baseBuilder().street(null).build();

        Set<ConstraintViolation<CreateAddressRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("street"));
    }

    @Test
    void missingCp_shouldCauseViolation() {
        CreateAddressRequestDTO dto = baseBuilder().cp(null).build();

        Set<ConstraintViolation<CreateAddressRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("cp"));
    }

    @Test
    void missingCity_shouldCauseViolation() {
        CreateAddressRequestDTO dto = baseBuilder().city(null).build();

        Set<ConstraintViolation<CreateAddressRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("city"));
    }

    @Test
    void missingCountry_shouldCauseViolation() {
        CreateAddressRequestDTO dto = baseBuilder().country(null).build();

        Set<ConstraintViolation<CreateAddressRequestDTO>> violations = validator.validate(dto);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("country"));
    }

    @Test
    void shouldReturnLocalizedValidationMessage_whenCityIsBlank() {
        // Simula que el locale activo es "es"
        LocaleContextHolder.setLocale(new Locale("es"));

        CreateAddressRequestDTO dto = baseBuilder().build();
        dto.setCity("");  // Campo inválido

        Set<ConstraintViolation<CreateAddressRequestDTO>> violations =
                validator.validate(dto); // javax.validation.Validator (inyectado con @Autowired)

        assertThat(violations).hasSize(1);
        String message = violations.iterator().next().getMessage();

        assertThat(message).isEqualTo("La ciudad es un campo obligatorio."); // mensaje definido en messages_es.properties
    }

    @Test
    void shouldReturnLocalizedValidationMessage_whenCountryIsBlank() {
        // Simula que el locale activo es "es"
        LocaleContextHolder.setLocale(new Locale("fr"));

        CreateAddressRequestDTO dto = baseBuilder().build();
        dto.setCountry("");  // Campo inválido

        Set<ConstraintViolation<CreateAddressRequestDTO>> violations =
                validator.validate(dto); // javax.validation.Validator (inyectado con @Autowired)

        assertThat(violations).hasSize(1);
        String message = violations.iterator().next().getMessage();

        assertThat(message).isEqualTo("Le pays est un champ obligatoire."); // mensaje definido en messages_fr.properties
    }

    @Test
    void shouldReturnLocalizedValidationMessage_whenCpIsBlank() {
        // Simula que el locale activo es "es"
        LocaleContextHolder.setLocale(new Locale("fr"));

        CreateAddressRequestDTO dto = baseBuilder().build();
        dto.setCp("");  // Campo inválido

        Set<ConstraintViolation<CreateAddressRequestDTO>> violations =
                validator.validate(dto); // javax.validation.Validator (inyectado con @Autowired)

        assertThat(violations).hasSize(1);
        String message = violations.iterator().next().getMessage();

        assertThat(message).isEqualTo("Le code postal est un champ obligatoire."); // mensaje definido en messages_fr.properties
    }

    @Test
    void shouldReturnLocalizedValidationMessage_whenStreetIsBlank() {
        // Simula que el locale activo es "es"
        LocaleContextHolder.setLocale(new Locale("es"));

        CreateAddressRequestDTO dto = baseBuilder().build();
        dto.setStreet("");  // Campo inválido

        Set<ConstraintViolation<CreateAddressRequestDTO>> violations =
                validator.validate(dto); // javax.validation.Validator (inyectado con @Autowired)

        assertThat(violations).hasSize(1);
        String message = violations.iterator().next().getMessage();

        assertThat(message).isEqualTo("La calle es un campo obligatorio."); // mensaje definido en messages_fr.properties
    }

    @Test
    void shouldReturnLocalizedValidationMessage_whenStreetNumberIsBlank() {
        // Simula que el locale activo es "es"
        LocaleContextHolder.setLocale(new Locale("es"));

        CreateAddressRequestDTO dto = baseBuilder().build();
        dto.setStNumber("");  // Campo inválido

        Set<ConstraintViolation<CreateAddressRequestDTO>> violations =
                validator.validate(dto); // javax.validation.Validator (inyectado con @Autowired)

        assertThat(violations).hasSize(1);
        String message = violations.iterator().next().getMessage();

        assertThat(message).isEqualTo("El número de calle es un campo obligatorio."); // mensaje definido en messages_fr.properties
    }



}
