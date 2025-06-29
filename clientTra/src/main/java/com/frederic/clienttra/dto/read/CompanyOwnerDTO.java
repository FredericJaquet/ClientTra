package com.frederic.clienttra.dto.read;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CompanyOwnerDTO {
    private int idCompany;
    private String vatNumber;
    private String comName;
    private String legalName;
    private String email;
    private String web;
    private String logoPath;
    private List<AddressDTO> addresses;
    private List<PhoneDTO> phones;
    private List<BankAccountDTO> bankAccounts;
    private List<UserForAdminDTO> users;

}
