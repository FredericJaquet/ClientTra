package com.frederic.clienttra.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class RegistrationRequestDTO {

    @NotBlank
    private String vatNumber;

    private String comName;

    @NotBlank
    private String legalName;

    @Email
    private String email;

    private String web;

    @NotNull
    private NewAddressDTO address;

    @NotBlank
    private String adminUsername;

    @NotBlank
    @Email
    private String adminEmail;

    @NotBlank
    private String adminPassword;

    private String preferredLanguage;

    private String preferredTheme;

    private Boolean darkMode;

}