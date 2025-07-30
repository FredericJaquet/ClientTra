package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.read.CashFlowReportDTO;
import com.frederic.clienttra.dto.read.InvoiceSummaryForCashFlowReportDTO;
import com.frederic.clienttra.dto.read.PartyReportDTO;
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

@Service
@RequiredArgsConstructor
public class CashFlowReportService {

    private final InvoiceForCashFlowReportRepository repository;
    private final CashFlowReportMapper mapper;
    private final CompanyServiceImpl companyService;

    public CashFlowReportDTO generate(LocalDate initDate, LocalDate endDate, DocumentType type) {
        Company onwerCompany = companyService.getCurrentCompanyOrThrow();
        List<InvoiceForCashFlowReportProjection> rawData = repository.findInvoicesForReport(initDate, endDate, onwerCompany.getIdCompany(), type);

        // Agrupar por idCompany (cliente o proveedor)
        Map<Integer, List<InvoiceForCashFlowReportProjection>> groupedByCompany = rawData.stream()
                .collect(Collectors.groupingBy(InvoiceForCashFlowReportProjection::getIdCompany));

        List<PartyReportDTO> parties = new ArrayList<>();
        double grandTotalNet = 0.0;

        for (Map.Entry<Integer, List<InvoiceForCashFlowReportProjection>> entry : groupedByCompany.entrySet()) {
            Integer companyId = entry.getKey();
            List<InvoiceForCashFlowReportProjection> companyInvoices = entry.getValue();

            // Obtener info básica del cliente/proveedor desde la primera factura
            InvoiceForCashFlowReportProjection first = companyInvoices.get(0);

            PartyReportDTO dto = mapper.toPartyReportDTO(
                    companyId,
                    first.getLegalName(),
                    first.getVatNumber(),
                    companyInvoices
            );

            // Calcular totales por empresa
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
