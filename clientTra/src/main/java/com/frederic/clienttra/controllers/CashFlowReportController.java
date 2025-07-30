package com.frederic.clienttra.controllers;


import com.frederic.clienttra.dto.read.CashFlowReportDTO;
import com.frederic.clienttra.enums.DocumentType;
import com.frederic.clienttra.services.CashFlowReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reports/cash-flow/")
@RequiredArgsConstructor
public class CashFlowReportController {

    private final CashFlowReportService cashFlowReportService;

    @GetMapping("/income")
    public ResponseEntity<CashFlowReportDTO> getIncomeReport(@RequestParam LocalDate initDate, @RequestParam LocalDate endDate){
        CashFlowReportDTO dto = cashFlowReportService.generate(initDate, endDate, DocumentType.INV_CUST);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/outcome")
    public ResponseEntity<CashFlowReportDTO> getOutcomeReport(@RequestParam LocalDate initDate, @RequestParam LocalDate endDate){
        CashFlowReportDTO dto = cashFlowReportService.generate(initDate, endDate, DocumentType.INV_PROV);
        return ResponseEntity.ok(dto);
    }


}