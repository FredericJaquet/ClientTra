package com.frederic.clienttra.dto.read;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Base DTO representing common company information.
 * Includes VAT number, commercial and legal names, contact details,
 * and associated collections such as phones, addresses, bank accounts,
 * contact persons, and schemes.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class BaseCompanyDTO {

    private Integer idCompany;
    private String vatNumber;
    private String comName;
    private String legalName;
    private String email;
    private String web;
    private List<PhoneDTO> phones;
    private List<AddressDTO> addresses;
    private List<BankAccountDTO> bankAccounts;
    private List<ContactPersonDTO> contactPersons;
    private List<SchemeDTO> schemes;
}
