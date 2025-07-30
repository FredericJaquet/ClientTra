package com.frederic.clienttra.mappers;

import com.frederic.clienttra.dto.read.InvoiceSummaryForPendingReportDTO;
import com.frederic.clienttra.dto.read.MonthlyPendingReportDTO;
import com.frederic.clienttra.projections.InvoiceForPendingReportProjection;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.util.List;

@Component
public class PendingReportMapper {

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
