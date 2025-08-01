package com.frederic.clienttra.dto.demo;

import com.frederic.clienttra.dto.create.*;
import com.frederic.clienttra.entities.ChangeRate;
import com.frederic.clienttra.entities.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO representing a demo company with complete sample data.
 * This includes company metadata, contact details, financial info, predefined schemes,
 * documents, and orders used for demonstration purposes.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DemoCompanyDTO {
    private String vatNumber;
    private String comName;
    private String legalName;
    private String email;
    private String web;
    private String defaultLanguage;
    private Double defaultVAT;
    private Double defaultWithholding;
    private String invoicingMethod;
    private String payMethod;
    private Integer duedate;
    private Boolean europe;
    private List<CreateContactPersonRequestDTO> contactPersons;
    private List<CreatePhoneRequestDTO> phones;
    private List<CreateAddressRequestDTO> addresses;
    private List<CreateBankAccountRequestDTO> bankAccounts;
    private List<CreateSchemeRequestDTO> schemes;
    private List<DemoDocumentDTO> documents;
    private List<CreateOrderRequestDTO> orders;
}
