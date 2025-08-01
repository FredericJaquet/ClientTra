package com.frederic.clienttra.dto.read;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO for representing cash flow report data.
 * Contains the date range, total net amount, and a list of parties involved in the report.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashFlowReportDTO {
    private LocalDate initDate;
    private LocalDate endDate;
    private Double grandTotalNet;
    private List<PartyForCashFlowReportDTO> parties;

}
