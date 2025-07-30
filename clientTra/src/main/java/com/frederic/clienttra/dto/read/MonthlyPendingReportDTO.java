package com.frederic.clienttra.dto.read;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyPendingReportDTO {
    private YearMonth deadline;
    private Double monthlyTotal;
    private List<InvoiceSummaryForPendingReportDTO> invoices;

}
