package com.frederic.clienttra.mappers;

import com.frederic.clienttra.dto.read.InvoiceSummaryForPendingReportDTO;
import com.frederic.clienttra.dto.read.MonthlyPendingReportDTO;
import com.frederic.clienttra.projections.InvoiceForPendingReportProjection;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.util.List;

/**
 * Mapper class to convert invoice projections into report DTOs
 * related to pending invoices grouped by month.
 */
@Component
public class PendingReportMapper {

    /**
     * Converts a list of invoice projections for a given month
     * into a MonthlyPendingReportDTO containing summarized invoice data.
     *
     * If the projection list is empty, returns null.
     *
     * @param ym the YearMonth for which the report is generated
     * @param projections list of InvoiceForPendingReportProjection objects
     * @return a MonthlyPendingReportDTO summarizing invoices for the month,
     *         or null if there are no invoices
     */
    public MonthlyPendingReportDTO toMonthlyPendingReportDTO(YearMonth ym, List<InvoiceForPendingReportProjection> projections){
        if(projections.isEmpty()){
            return null;
        }

        List<InvoiceSummaryForPendingReportDTO> invoices = projections.stream()
                .map(p -> new InvoiceSummaryForPendingReportDTO(
                        p.getIdDocument(),
                        p.getComName(),
                        p.getDocNumber(),
                        p.getTotalToPay(),
                        p.getStatus()
                ))
                .toList();

        return MonthlyPendingReportDTO.builder()
                .monthlyTotal(0.0)
                .deadline(ym)
                .invoices(invoices)
                .build();
    }

}
