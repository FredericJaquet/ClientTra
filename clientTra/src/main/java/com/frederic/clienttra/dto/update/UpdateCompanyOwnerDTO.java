package com.frederic.clienttra.dto.update;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UpdateCompanyOwnerDTO {
    private String vatNumber;
    private String comName;
    private String legalName;
    private String email;
    private String web;
    private String logoPath;
    private List<UpdateAddressRequestDTO> addresses;
    private List<UpdatePhoneRequestDTO> phones;
    private List<UpdateBankAccountRequestDTO> bankAccounts;
}
