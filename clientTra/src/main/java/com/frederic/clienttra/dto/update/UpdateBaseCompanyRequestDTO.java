package com.frederic.clienttra.dto.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UpdateBaseCompanyRequestDTO {
    protected String vatNumber;
    protected String comName;
    protected String legalName;
    protected String email;
    protected String web;
    /*private List<UpdateContactPersonRequestDTO> contactPersons;
    private List<UpdatePhoneRequestDTO> phones;
    private List<UpdateAddressRequestDTO> addresses;
    private List<UpdateBankAccountRequestDTO> bankAccounts;*/
}
