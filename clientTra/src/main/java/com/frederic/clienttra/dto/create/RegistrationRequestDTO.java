package com.frederic.clienttra.dto.create;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationRequestDTO {
    @NotBlank(message = "validation.company.vat_number_required")
    private String vatNumber;
    private String comName;
    @NotBlank(message = "validation.company.legal_name_required")
    private String legalName;
    @Email(message = "validation.email.invalid")
    @Size(max = 100, message = "validation.email.too_long")
    private String email;
    private String web;
    @NotNull(message = "validation.company.address_required")
    private CreateAddressRequestDTO address;
    @NotBlank(message = "validation.username.required")
    private String adminUsername;
    @Email(message = "validation.email.invalid")
    @Size(max = 100, message = "validation.email.too_long")
    private String adminEmail;
    @NotBlank(message = "validation.password.required")
    private String adminPassword;
    private String preferredLanguage;
    private String preferredTheme;
    private Boolean darkMode;

}