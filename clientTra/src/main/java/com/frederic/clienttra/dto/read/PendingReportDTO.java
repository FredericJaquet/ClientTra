package com.frederic.clienttra.dto.read;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for a pending report.
 * Contains the grand total amount and a list of monthly pending reports.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PendingReportDTO {
    private Double grandTotal;
    private List<MonthlyPendingReportDTO> monthlyReports;

}
