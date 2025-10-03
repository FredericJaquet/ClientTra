package com.frederic.clienttra.dto.read;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO for representing monthly cash flow report data.
 * Contains the date range, total net amount, and a list of monthly Reports.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CashFlowGraphDTO {
    private LocalDate initDate;
    private LocalDate endDate;
    private Double grandTotalNet;
    private Double averageNet;
    private List<MonthlyForCashFlowGraphDTO> months;
}
