package com.frederic.clienttra.controllers;

import com.frederic.clienttra.dto.create.CreateDocumentRequestDTO;
import com.frederic.clienttra.dto.read.DocumentDTO;
import com.frederic.clienttra.dto.read.DocumentForListDTO;
import com.frederic.clienttra.enums.DocumentStatus;
import com.frederic.clienttra.enums.DocumentType;
import com.frederic.clienttra.services.DocumentService;
import com.frederic.clienttra.utils.DocumentUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer-invoices")
@RequiredArgsConstructor
public class CustomerInvoiceController {

    private final DocumentService documentService;
    private final DocumentUtils documentUtils;
    private static final DocumentType DOC_TYPE = DocumentType.INV_CUST;

    @GetMapping
    public ResponseEntity<List<DocumentForListDTO>> getAllCustomerInvoices() {
        List<DocumentForListDTO> result = documentService.getAllDocumentsByType(DOC_TYPE);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/by-customer/{idCustomer}")
    public ResponseEntity<List<DocumentForListDTO>> getCustomerInvoicesByCustomer(@PathVariable Integer idCustomer) {
        List<DocumentForListDTO> result = documentService.getDocumentsByCustomerId(DOC_TYPE, idCustomer);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/by-status")
    public ResponseEntity<List<DocumentForListDTO>> getCustomerInvoicesByStatus(@RequestParam DocumentStatus status) {
        List<DocumentForListDTO> result = documentService.getDocumentsByStatus(DOC_TYPE, status);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/by-customer/{idCustomer}/by-status")
    public ResponseEntity<List<DocumentForListDTO>> getCustomerInvoicesByCustomerAndStatus(
            @PathVariable Integer idCustomer,
            @RequestParam DocumentStatus status) {
        List<DocumentForListDTO> result = documentService.getDocumentsByCustomerAndStatus(DOC_TYPE, idCustomer, status);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/create/{idCompany}")
    public ResponseEntity<DocumentDTO> createCustomerInvoice(@PathVariable Integer idCompany,
                                                             @RequestBody CreateDocumentRequestDTO dto) {
        DocumentDTO created = documentService.createDocumentForCustomer(idCompany, dto, DOC_TYPE);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/last-number")
    public ResponseEntity<String> getLastCustomerInvoiceNumber() {
        String lastNumber = documentService.getLastDocumentNumber(DOC_TYPE);
        return ResponseEntity.ok(lastNumber);
    }
}