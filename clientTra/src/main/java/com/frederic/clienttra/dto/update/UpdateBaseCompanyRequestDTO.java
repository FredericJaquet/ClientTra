package com.frederic.clienttra.dto.update;

import com.frederic.clienttra.dto.read.AddressDTO;
import com.frederic.clienttra.dto.read.ContactPersonDTO;
import com.frederic.clienttra.dto.read.PhoneDTO;
import lombok.Data;

import java.util.List;

@Data
public class UpdateBaseCompanyRequestDTO {
    protected String vatNumber;
    protected String comName;
    protected String legalName;
    protected String email;
    protected String web;
    private List<ContactPersonDTO> contactPersons;
    private List<PhoneDTO> phones;
    private List<AddressDTO> addresses;
    private List<UpdateBankAccountRequestDTO> bankAccounts;
}
