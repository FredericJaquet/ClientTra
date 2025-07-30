package com.frederic.clienttra.controllers;

import com.frederic.clienttra.dto.read.PendingReportDTO;
import com.frederic.clienttra.enums.DocumentType;
import com.frederic.clienttra.services.PendingReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports/pending/")
@RequiredArgsConstructor
public class PendingReportController {

    private final PendingReportService service;

    @GetMapping("/income")
    public ResponseEntity<PendingReportDTO> getPendingIncomeReport(){
        PendingReportDTO dto = service.generate(DocumentType.INV_CUST);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/outcome")
    public ResponseEntity<PendingReportDTO> getPendingOutcomeReport(){
        PendingReportDTO dto = service.generate(DocumentType.INV_PROV);
        return ResponseEntity.ok(dto);
    }

}