package com.frederic.clienttra.dto.demo;

import com.frederic.clienttra.dto.create.CreateAddressRequestDTO;
import com.frederic.clienttra.dto.create.CreateBankAccountRequestDTO;
import com.frederic.clienttra.dto.create.CreateChangeRateRequestDTO;
import com.frederic.clienttra.entities.ChangeRate;
import lombok.Data;

import java.util.List;

@Data
public class DemoOwnerCompanyDTO {
    private String vatNumber;
    private String comName;
    private String legalName;
    private String email;
    private String web;

    private List<CreateAddressRequestDTO> addresses;
    private List<CreateBankAccountRequestDTO> bankAccounts;
    private List<CreateChangeRateRequestDTO> changeRates;
}
