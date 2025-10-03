package com.frederic.clienttra.mappers;

import com.frederic.clienttra.dto.read.CashFlowGraphDTO;
import com.frederic.clienttra.dto.read.InvoiceSummaryForCashFlowReportDTO;
import com.frederic.clienttra.dto.read.MonthlyForCashFlowGraphDTO;
import com.frederic.clienttra.dto.read.PartyForCashFlowReportDTO;
import com.frederic.clienttra.projections.InvoiceForCashFlowGraphProjection;
import com.frederic.clienttra.projections.InvoiceForCashFlowReportProjection;
import org.springframework.stereotype.Component;

import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper class for converting invoice and party projections into DTOs
 * used in cash flow reports.
 */
@Component
public class CashFlowReportMapper {

    /**
     * Converts invoice projections and company info into a PartyForCashFlowReportDTO.
     * If the list of projections is empty, returns null.
     *
     * Note: Totals (totalNet, totalVat, totalWithholding) are initialized to 0.0
     * and calculated later in the service layer.
     *
     * @param idCompany the ID of the company
     * @param legalName the legal name of the party
     * @param vatNumber the VAT number of the party
     * @param projections list of invoice projections related to the party
     * @return a PartyForCashFlowReportDTO containing the party and its invoices, or null if no invoices
     */
    public PartyForCashFlowReportDTO toPartyReportDTO(Integer idCompany, String legalName, String vatNumber, List<InvoiceForCashFlowReportProjection> projections) {
        if (projections.isEmpty()){
            return null;
        }

        List<InvoiceSummaryForCashFlowReportDTO> invoices = projections.stream()
                .map(p -> new InvoiceSummaryForCashFlowReportDTO(
                        p.getInvoiceNumber(),
                        p.getDocDate(),
                        p.getTotalNet(),
                        p.getTotalVat(),
                        p.getTotalWithholding()
                ))
                .collect(Collectors.toList());

        return PartyForCashFlowReportDTO.builder()
                .idCompany(idCompany)
                .legalName(legalName)
                .vatNumber(vatNumber)
                .totalNet(0.0)
                .totalVat(0.0)
                .totalWithholding(0.0) // Totals are calculated in the Service layer
                .invoices(invoices)
                .build();
    }

}
