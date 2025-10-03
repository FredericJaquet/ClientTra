package com.frederic.clienttra.dto.read;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

/**
 * DTO for representing a monthly report in a cash flow report for Graph.
 * Contains time details and monthly total.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyForCashFlowGraphDTO {
    private YearMonth docDate;
    private Double totalNet;
}
