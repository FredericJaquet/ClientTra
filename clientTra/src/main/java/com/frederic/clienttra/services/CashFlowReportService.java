package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.read.CashFlowReportDTO;
import com.frederic.clienttra.dto.read.InvoiceSummaryForCashFlowReportDTO;
import com.frederic.clienttra.dto.read.PartyForCashFlowReportDTO;
import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.enums.DocumentType;
import com.frederic.clienttra.mappers.CashFlowReportMapper;
import com.frederic.clienttra.projections.InvoiceForCashFlowReportProjection;
import com.frederic.clienttra.repositories.InvoiceForCashFlowReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for generating cash flow reports based on invoices within a date range.
 * <p>
 * It retrieves invoices filtered by document type and owner company, groups them by client or provider company,
 * calculates totals, and maps data into DTOs for reporting purposes.
 */
@Service
@RequiredArgsConstructor
public class CashFlowReportService {

    private final InvoiceForCashFlowReportRepository repository;
    private final CashFlowReportMapper mapper;
    private final CompanyServiceImpl companyService;

    /**
     * Generates a cash flow report for invoices of a specified document type between two dates.
     *
     * @param initDate the start date (inclusive) of the report period
     * @param endDate  the end date (inclusive) of the report period
     * @param type     the document type to filter invoices (e.g., INVOICE)
     * @return a {@link CashFlowReportDTO} containing aggregated invoice data grouped by client/provider
     * @throws RuntimeException if the current user's company cannot be retrieved
     */
    public CashFlowReportDTO generate(LocalDate initDate, LocalDate endDate, DocumentType type) {
        Company onwerCompany = companyService.getCurrentCompanyOrThrow();
        List<InvoiceForCashFlowReportProjection> rawData = repository.findInvoicesForCashFlowReport(initDate, endDate, onwerCompany.getIdCompany(), type);

        System.out.println(rawData.size());

        // Group invoices by company (client or provider)
        Map<Integer, List<InvoiceForCashFlowReportProjection>> groupedByCompany = rawData.stream()
                .collect(Collectors.groupingBy(InvoiceForCashFlowReportProjection::getIdCompany));

        List<PartyForCashFlowReportDTO> parties = new ArrayList<>();
        double grandTotalNet = 0.0;

        for (Map.Entry<Integer, List<InvoiceForCashFlowReportProjection>> entry : groupedByCompany.entrySet()) {
            Integer companyId = entry.getKey();
            List<InvoiceForCashFlowReportProjection> companyInvoices = entry.getValue();

            // Retrieve basic company info from the first invoice of the group
            InvoiceForCashFlowReportProjection first = companyInvoices.get(0);

            PartyForCashFlowReportDTO dto = mapper.toPartyReportDTO(
                    companyId,
                    first.getLegalName(),
                    first.getVatNumber(),
                    companyInvoices
            );

            // Calculate totals per company
            double totalNet = dto.getInvoices().stream().mapToDouble(i -> Optional.ofNullable(i.getTotalNet()).orElse(0.0)).sum();
            double totalVat = dto.getInvoices().stream().mapToDouble(i -> Optional.ofNullable(i.getTotalVat()).orElse(0.0)).sum();
            double totalWithholding = dto.getInvoices().stream().mapToDouble(i -> Optional.ofNullable(i.getTotalWithholding()).orElse(0.0)).sum();

            dto.setTotalNet(totalNet);
            dto.setTotalVat(totalVat);
            dto.setTotalWithholding(totalWithholding);

            dto.setInvoices(dto.getInvoices().stream()
                    .sorted(Comparator.comparing(InvoiceSummaryForCashFlowReportDTO::getInvoiceNumber))
                    .toList());

            parties.add(dto);

            grandTotalNet += totalNet;
        }

        return CashFlowReportDTO.builder()
                .initDate(initDate)
                .endDate(endDate)
                .grandTotalNet(grandTotalNet)
                .parties(parties)
                .build();
    }

}
