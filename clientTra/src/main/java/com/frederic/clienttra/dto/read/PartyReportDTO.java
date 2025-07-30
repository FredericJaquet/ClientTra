package com.frederic.clienttra.dto.read;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartyReportDTO {
    private Integer idCompany;
    private String legalName;
    private String vatNumber;
    private Double totalNet;
    private Double totalVat;
    private Double totalWithholding;
    private List<InvoiceSummaryForCashFlowReportDTO> invoices;

}
