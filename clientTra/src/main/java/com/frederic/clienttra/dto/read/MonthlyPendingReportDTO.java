package com.frederic.clienttra.dto.read;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;
import java.util.List;

/**
 * DTO for representing a monthly pending report.
 * Contains the deadline month, the total amount pending for that month,
 * and a list of invoice summaries that are pending.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyPendingReportDTO {
    private YearMonth deadline;
    private Double monthlyTotal;
    private List<InvoiceSummaryForPendingReportDTO> invoices;

}
