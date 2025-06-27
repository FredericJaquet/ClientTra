package com.frederic.clienttra.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UpdateCompanyOwnerDTO {
    private String vatNumber;
    private String comName;
    private String legalName;
    @Email(message = "validation.email.invalid")
    @Size(max = 100, message = "validation.email.too_long")
    private String email;
    private String web;
    private String logoPath;
    private List<AddressDTO> addresses;
    private List<PhoneDTO> phones;
    @Valid
    private List<BankAccountDTO> bankAccounts;
}
