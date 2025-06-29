package com.frederic.clienttra.dto.read;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CustomerDetailsDTO {

    private Integer idCustomer;
    private String vatNumber;
    private String comName;
    private String legaName;
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
    private double defaultVat;
    private double defaultWithholding;
    private Boolean europe;
    private Boolean enabled;







}
