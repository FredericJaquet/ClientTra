package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.read.InvoiceSummaryForPendingReportDTO;
import com.frederic.clienttra.dto.read.MonthlyPendingReportDTO;
import com.frederic.clienttra.dto.read.PendingReportDTO;
import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.enums.DocumentStatus;
import com.frederic.clienttra.enums.DocumentType;
import com.frederic.clienttra.mappers.PendingReportMapper;
import com.frederic.clienttra.projections.InvoiceForPendingReportProjection;
import com.frederic.clienttra.projections.OrderListForDashboardProjection;
import com.frederic.clienttra.repositories.InvoiceForPendingReportRepository;
import com.frederic.clienttra.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service responsible for generating pending invoice reports grouped by month.
 */
@Service
@RequiredArgsConstructor
public class PendingReportService {

    private final InvoiceForPendingReportRepository repository;
    private final PendingReportMapper mapper;
    private final CompanyServiceImpl companyService;

    /**
     * Generates a pending report for invoices of the specified document type.
     * The report groups pending invoices by YearMonth and calculates totals per month and overall.
     *
     * @param type the document type to generate the report for (e.g., customer or provider invoices).
     * @return a DTO containing the aggregated pending report data.
     */
    public PendingReportDTO generate(DocumentType type){
        // Retrieve the current user's owning company
        Company owner = companyService.getCurrentCompanyOrThrow();

        // Fetch raw pending invoices for the company and document type
        List<InvoiceForPendingReportProjection> rawData = repository.findInvoiceForPendingReport(owner.getIdCompany(), type, DocumentStatus.PENDING);

        // Group invoices by YearMonth of the document date
        Map<YearMonth, List<InvoiceForPendingReportProjection>> groupedByYearMonth =  rawData.stream()
                .collect(Collectors.groupingBy(p -> YearMonth.from(p.getDeadline())));

        List<MonthlyPendingReportDTO> monthlyReport =  new ArrayList<>();
        double grandTotal = 0.0;

        // Process each YearMonth group to build the monthly report DTOs
        for(Map.Entry<YearMonth, List<InvoiceForPendingReportProjection>> entry : groupedByYearMonth.entrySet()){
            YearMonth ym = entry.getKey();
            List<InvoiceForPendingReportProjection> monthlyInvoices = entry.getValue();

            if (monthlyInvoices.isEmpty()) continue;

            // Map projections to monthly report DTO
            MonthlyPendingReportDTO dto = mapper.toMonthlyPendingReportDTO(ym, monthlyInvoices);

            // Calculate total amount pending for the month
            double monthlyTotal = dto.getInvoices().stream()
                    .mapToDouble(i -> Optional.ofNullable(i.getTotalToPay()).orElse(0.0))
                    .sum();
            dto.setMonthlyTotal(monthlyTotal);

            // Sort invoices within the month by document number
            dto.setInvoices(dto.getInvoices().stream()
                    .sorted(Comparator.comparing(InvoiceSummaryForPendingReportDTO::getDocNumber))
                    .toList());

            monthlyReport.add(dto);
            grandTotal += monthlyTotal;
        }

        // Build and return the complete pending report DTO
        return PendingReportDTO.builder()
                .grandTotal(grandTotal)
                .monthlyReports(monthlyReport)
                .build();
    }

}
