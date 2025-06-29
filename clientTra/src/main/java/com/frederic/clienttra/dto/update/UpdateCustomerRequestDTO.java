package com.frederic.clienttra.dto.update;

import lombok.Data;

import java.util.List;

@Data
public class UpdateCustomerRequestDTO {
    //BaseCompany fields
    private String vatNumber;
    private String comName;
    private String legalName;
    private String email;
    private String web;
    private List<UpdatePhoneRequestDTO> phones;
    private List<UpdateAddressRequestDTO> addresses;
    private List<UpdateBankAccountRequestDTO> bankAccounts;
    private List<UpdateContactPersonRequestDTO> contactPersons;
    //Customer fields
    private String invoicingMethod;
    private Integer duedate;
    private String payMethod;
    private String defaultLanguage;
    private Double defaultVAT;
    private Double defaultWithholding;
    private Boolean europe;

    private Boolean enabled;
}
