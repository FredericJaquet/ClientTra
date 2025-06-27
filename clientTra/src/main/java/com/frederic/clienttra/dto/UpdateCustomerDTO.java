package com.frederic.clienttra.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class UpdateCustomerDTO {

    @NotBlank
    private String vatNumber;
    private String comName;
    @NotBlank
    private String legalName;
    private String email;
    private String web;
    private List<PhoneDTO> phones;
    private List<AddressDTO> addresses;
    private List<BankAccountDTO> bankAccounts;
    private List<ContactPersonDTO> contactPersons;

    private String invoicingMethod;

    private Integer duedate;

    private String payMethod;

    private String defaultLanguage;

    private Double defaultVAT;

    private Double defaultWithholding;

    private Boolean europe;

    private Boolean enabled;
}
