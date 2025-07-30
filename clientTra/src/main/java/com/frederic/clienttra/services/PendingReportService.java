package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.read.InvoiceSummaryForPendingReportDTO;
import com.frederic.clienttra.dto.read.MonthlyPendingReportDTO;
import com.frederic.clienttra.dto.read.PendingReportDTO;
import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.enums.DocumentStatus;
import com.frederic.clienttra.enums.DocumentType;
import com.frederic.clienttra.mappers.PendingReportMapper;
import com.frederic.clienttra.projections.InvoiceForPendingReportProjection;
import com.frederic.clienttra.repositories.InvoiceForPendingReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PendingReportService {

    private final InvoiceForPendingReportRepository repository;
    private final PendingReportMapper mapper;
    private final CompanyServiceImpl companyService;

    public PendingReportDTO generate(DocumentType type){
        Company owner = companyService.getCurrentCompanyOrThrow();
        List<InvoiceForPendingReportProjection> rawData = repository.findInvoiceForPendingReport(owner.getIdCompany(), type, DocumentStatus.PENDING);

        // Agrupar por YearMonth
        Map<YearMonth, List<InvoiceForPendingReportProjection>> groupedByYearMonth =  rawData.stream()
                .collect(Collectors.groupingBy(p -> YearMonth.from(p.getDocDate())));
        List<MonthlyPendingReportDTO> monthlyReport =  new ArrayList<>();
        double grandTotal = 0.0;

        for(Map.Entry<YearMonth, List<InvoiceForPendingReportProjection>> entry : groupedByYearMonth.entrySet()){
            YearMonth ym = entry.getKey();
            List<InvoiceForPendingReportProjection> monthlyInvoices = entry.getValue();

            if (monthlyInvoices.isEmpty()) continue;

            MonthlyPendingReportDTO dto = mapper.toMonthlyPendingReportDTO(ym,monthlyInvoices);

            //Calcular totales por mes
            double monthlyTotal = dto.getInvoices().stream().mapToDouble(i -> Optional.ofNullable(i.getTotalToPay()).orElse(0.0)).sum();
            dto.setMonthlyTotal(monthlyTotal);

            dto.setInvoices(dto.getInvoices().stream()
                    .sorted(Comparator.comparing(InvoiceSummaryForPendingReportDTO::getDocNumber))
                    .toList());

            monthlyReport.add(dto);
            grandTotal += monthlyTotal;
        }

        return PendingReportDTO.builder()
                .grandTotal(grandTotal)
                .monthlyReports(monthlyReport)
                .build();
    }

}
