package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.read.*;
import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.enums.DocumentType;
import com.frederic.clienttra.mappers.CashFlowReportMapper;
import com.frederic.clienttra.projections.InvoiceForCashFlowGraphProjection;
import com.frederic.clienttra.projections.InvoiceForCashFlowReportProjection;
import com.frederic.clienttra.projections.PendingOrdersForCashflowReportProjection;
import com.frederic.clienttra.repositories.InvoiceForCashFlowReportRepository;
import com.frederic.clienttra.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
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
    private final OrderRepository orderRepository;
    private final CashFlowReportMapper mapper;
    private final OrderService orderService;
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
    public CashFlowReportDTO generateReport(LocalDate initDate, LocalDate endDate, DocumentType type, Boolean withOrders) {
        Company onwerCompany = companyService.getCurrentCompanyOrThrow();
        List<InvoiceForCashFlowReportProjection> rawData = repository.findInvoicesForCashFlowReport(initDate, endDate, onwerCompany.getIdCompany(), type);
        List<PendingOrdersForCashflowReportProjection> pendingOrders = null;

       // Group invoices by company (client or provider)
        Map<Integer, List<InvoiceForCashFlowReportProjection>> groupedByCompany = rawData.stream()
                .collect(Collectors.groupingBy(InvoiceForCashFlowReportProjection::getIdCompany));

        double grandTotalNet = 0.0;

        List<PartyForCashFlowReportDTO> parties = new ArrayList<>();
        Map<Integer, InvoiceSummaryForCashFlowReportDTO> fakeInvoices = new HashMap<>();

        if(withOrders) {
            pendingOrders = orderRepository.findByOwnerCompanyPendingOrdersForCustomersByDateOrderDesc(onwerCompany);
            //Group orders by company (client or provider)
            Map<Integer, List<PendingOrdersForCashflowReportProjection>> ordersGroupedByCompany = pendingOrders.stream()
                    .collect(Collectors.groupingBy(PendingOrdersForCashflowReportProjection::getIdCompany));
            //Adding orders grouped by company as "Fake" invoices"
            for (Map.Entry<Integer, List<PendingOrdersForCashflowReportProjection>> entry : ordersGroupedByCompany.entrySet()) {
                List<PendingOrdersForCashflowReportProjection> orders = entry.getValue();
                Integer idCompany = entry.getKey();
                double totalNetOrders = orders.stream().mapToDouble(PendingOrdersForCashflowReportProjection::getTotal).sum();

                InvoiceSummaryForCashFlowReportDTO invoice = InvoiceSummaryForCashFlowReportDTO.builder()
                        .invoiceNumber("PENDING")
                        .docDate(LocalDate.now())
                        .totalNet(totalNetOrders)
                        .totalVat(0.0)
                        .totalWithholding(0.0)
                        .build();

                fakeInvoices.put(idCompany, invoice);
            }
        }

        for (Map.Entry<Integer, List<InvoiceForCashFlowReportProjection>> entry : groupedByCompany.entrySet()) {
            Integer idCompany = entry.getKey();
            List<InvoiceForCashFlowReportProjection> companyInvoices = entry.getValue();

            // Retrieve basic company info from the first invoice of the group
            InvoiceForCashFlowReportProjection first = companyInvoices.get(0);

            PartyForCashFlowReportDTO dto = mapper.toPartyReportDTO(
                    idCompany,
                    first.getLegalName(),
                    first.getVatNumber(),
                    companyInvoices
            );

            if(fakeInvoices.get(idCompany) != null) {
                dto.getInvoices().add(fakeInvoices.get(idCompany));
            }

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

    /**
     * Generates a cash flow graph for invoices of a specified document type between two dates.
     *
     * @param initDate the start date (inclusive) of the report period
     * @param endDate  the end date (inclusive) of the report period
     * @param type     the document type to filter invoices (e.g., INVOICE)
     * @return a {@link CashFlowGraphDTO} containing aggregated invoice data grouped by client/provider
     * @throws RuntimeException if the current user's company cannot be retrieved
     */
    public CashFlowGraphDTO generateGraph(LocalDate initDate, LocalDate endDate, DocumentType type) {
        Company ownerCompany = companyService.getCurrentCompanyOrThrow();
        List<InvoiceForCashFlowGraphProjection> rawData = repository.findInvoicesForCashFlowGraph(
                initDate, endDate, ownerCompany.getIdCompany(), type);

        // Agrupar por YearMonth, ordenado
        Map<YearMonth, List<InvoiceForCashFlowGraphProjection>> groupedByYearMonth =
                rawData.stream()
                        .collect(Collectors.groupingBy(
                                p -> YearMonth.from(p.getDocDate()),
                                TreeMap::new,
                                Collectors.toList()
                        ));

        List<MonthlyForCashFlowGraphDTO> monthlyInvoicesForGraph = new ArrayList<>();
        double grandTotal = 0.0;

        YearMonth startYm = YearMonth.from(initDate);
        YearMonth endYm = YearMonth.from(endDate);

        // Generar todos los meses del rango
        for (YearMonth ym = startYm; !ym.isAfter(endYm); ym = ym.plusMonths(1)) {
            double monthlyTotal = groupedByYearMonth.getOrDefault(ym, List.of())
                    .stream()
                    .mapToDouble(i -> Optional.ofNullable(i.getTotalNet()).orElse(0.0))
                    .sum();

            monthlyInvoicesForGraph.add(
                    MonthlyForCashFlowGraphDTO.builder()
                            .docDate(ym)
                            .totalNet(monthlyTotal)
                            .build()
            );

            grandTotal += monthlyTotal;
        }

        long months = ChronoUnit.MONTHS.between(startYm, endYm) + 1;
        double averageNet = months > 0 ? grandTotal / months : 0.0;

        return CashFlowGraphDTO.builder()
                .initDate(initDate)
                .endDate(endDate)
                .grandTotalNet(grandTotal)
                .averageNet(averageNet)
                .months(monthlyInvoicesForGraph)
                .build();
    }
}
