package com.frederic.clienttra.dto.read;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceSummaryForCashFlowReportDTO {
    private String invoiceNumber;
    private LocalDate docDate;
    private Double totalNet;
    private Double totalVat;
    private Double totalWithholding;

}
