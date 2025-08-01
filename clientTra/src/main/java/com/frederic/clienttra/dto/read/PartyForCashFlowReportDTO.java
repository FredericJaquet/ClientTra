package com.frederic.clienttra.dto.read;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for representing a party (company) in a cash flow report.
 * Contains company identification and financial summary along with a list of invoices.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartyForCashFlowReportDTO {
    private Integer idCompany;
    private String legalName;
    private String vatNumber;
    private Double totalNet;
    private Double totalVat;
    private Double totalWithholding;
    private List<InvoiceSummaryForCashFlowReportDTO> invoices;

}
