package com.frederic.clienttra.dto.create;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegistrationRequestDTO {//TODO validation message (see CreateUserRequestDTO)
    @NotBlank
    private String vatNumber;
    private String comName;
    @NotBlank
    private String legalName;
    @Email
    private String email;
    private String web;
    @NotNull
    private CreateAddressRequestDTO address;
    @NotBlank
    private String adminUsername;
    @Email(message = "validation.email.invalid")
    @Size(max = 100, message = "validation.email.too_long")
    private String adminEmail;
    @NotBlank
    private String adminPassword;
    private String preferredLanguage;
    private String preferredTheme;
    private Boolean darkMode;

}