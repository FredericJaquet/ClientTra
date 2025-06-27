package com.frederic.clienttra.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class BaseCompanyRequestDTO {
    @NotBlank
    protected String vatNumber;
    protected String comName;
    @NotBlank
    protected String legalName;
    protected String email;
    protected String web;
    private List<ContactPersonDTO> contactPersons;
    private List<PhoneDTO> phones;
    private List<AddressDTO> addresses;
    private List<BankAccountDTO> bankAccounts;
}