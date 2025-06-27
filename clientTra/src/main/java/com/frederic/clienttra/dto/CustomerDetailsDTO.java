package com.frederic.clienttra.dto;

import com.frederic.clienttra.entities.ContactPerson;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CustomerDetailsDTO {

    private Integer idCustomer;
    private String comName;
    private String legaName;
    private String web;
    private String email;
    private String invoicingMethod;
    private String payMethod;
    private String defaultLanguage;
    private String vatNumber;
    private double defaultVat;
    private double defaultWithholding;
    private Integer duedate;
    private Boolean europe;
    private Boolean enabled;

    private List<AddressDTO> addresses;
    private List<ContactPersonDTO> contactPersons;
    private List<PhoneDTO> phones;
    @Valid
    private List<BankAccountDTO> bankAccounts;


}
