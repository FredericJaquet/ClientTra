package com.frederic.clienttra.dto.create;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CreateBaseCompanyRequestDTO {//TODO validation message (see CreateUserRequestDTO)
    @NotBlank
    protected String vatNumber;
    protected String comName;
    @NotBlank
    protected String legalName;
    @Email(message = "validation.email.invalid")
    @Size(max = 100, message = "validation.email.too_long")
    protected String email;
    protected String web;
    private List<CreateContactPersonRequestDTO> contactPersons;
    private List<CreatePhoneRequestDTO> phones;
    @NotNull
    private List<CreateAddressRequestDTO> addresses;
    private List<CreateBankAccountRequestDTO> bankAccounts;
}