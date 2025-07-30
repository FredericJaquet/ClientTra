package com.frederic.clienttra.mappers;

import com.frederic.clienttra.dto.read.InvoiceSummaryForCashFlowReportDTO;
import com.frederic.clienttra.dto.read.PartyReportDTO;
import com.frederic.clienttra.projections.InvoiceForCashFlowReportProjection;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CashFlowReportMapper {

    public PartyReportDTO toPartyReportDTO(Integer idCompany, String legalName, String vatNumber, List<InvoiceForCashFlowReportProjection> projections) {
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
                .toList();

        return PartyReportDTO.builder()
                .idCompany(idCompany)
                .legalName(legalName)
                .vatNumber(vatNumber)
                .totalNet(0.0)
                .totalVat(0.0)
                .totalWithholding(0.0)//Los totales se calculan en el Service
                .invoices(invoices)
                .build();
    }

}