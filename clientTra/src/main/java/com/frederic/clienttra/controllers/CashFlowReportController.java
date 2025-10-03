package com.frederic.clienttra.controllers;

import com.frederic.clienttra.dto.read.CashFlowReportDTO;
import com.frederic.clienttra.dto.read.CashFlowGraphDTO;
import com.frederic.clienttra.enums.DocumentType;
import com.frederic.clienttra.services.CashFlowReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * REST controller for generating cash flow reports.
 * <p>
 * Provides endpoints to generate income and outcome reports
 * within a specified date range.
 */
@RestController
@RequestMapping("/api/reports/cash-flow/")
@RequiredArgsConstructor
public class CashFlowReportController {

    private final CashFlowReportService service;

    /**
     * Generates an income report (based on customer invoices) for the given date range.
     *
     * @param initDate the start date of the report
     * @param endDate  the end date of the report
     * @return a {@link CashFlowReportDTO} representing the income report
     */
    @GetMapping("/income")
    public ResponseEntity<CashFlowReportDTO> getIncomeReport(@RequestParam LocalDate initDate, @RequestParam LocalDate endDate, @RequestParam Boolean withOrders) {
        // Uses DocumentType.INV_CUST to retrieve customer invoice data
        CashFlowReportDTO dto = service.generateReport(initDate, endDate, DocumentType.INV_CUST, withOrders);
        return ResponseEntity.ok(dto);
    }

    /**
     * Generates an outcome report (based on provider invoices) for the given date range.
     *
     * @param initDate the start date of the report
     * @param endDate  the end date of the report
     * @return a {@link CashFlowReportDTO} representing the outcome report
     */
    @GetMapping("/outcome")
    public ResponseEntity<CashFlowReportDTO> getOutcomeReport(@RequestParam LocalDate initDate, @RequestParam LocalDate endDate, @RequestParam Boolean withOrders) {
        // Uses DocumentType.INV_PROV to retrieve provider invoice data
        CashFlowReportDTO dto = service.generateReport(initDate, endDate, DocumentType.INV_PROV, withOrders);
        return ResponseEntity.ok(dto);
    }

    /**
     * Generates an income report (based on monthly incomes) for the given date range.
     *
     * @param initDate the start date of the report
     * @param endDate  the end date of the report
     * @return a {@link CashFlowGraphDTO} representing the income report
     */
    @GetMapping("/income/graph")
    public ResponseEntity<CashFlowGraphDTO> getIncomeReportGraph(@RequestParam LocalDate initDate, @RequestParam LocalDate endDate) {
        // Uses DocumentType.INV_CUST to retrieve customer invoice data
        CashFlowGraphDTO dto = service.generateGraph(initDate, endDate, DocumentType.INV_CUST);
        return ResponseEntity.ok(dto);
    }

    /**
     * Generates an outcome report (based on monthly outcomes) for the given date range.
     *
     * @param initDate the start date of the report
     * @param endDate  the end date of the report
     * @return a {@link CashFlowGraphDTO} representing the outcome report
     */
    @GetMapping("/outcome/graph")
    public ResponseEntity<CashFlowGraphDTO> getOutcomeReportGraph(@RequestParam LocalDate initDate, @RequestParam LocalDate endDate) {
        // Uses DocumentType.INV_PROV to retrieve customer invoice data
        CashFlowGraphDTO dto = service.generateGraph(initDate, endDate, DocumentType.INV_PROV);
        return ResponseEntity.ok(dto);
    }

}
