package com.frederic.clienttra.controllers;

import com.frederic.clienttra.dto.GenericResponseDTO;
import com.frederic.clienttra.dto.create.CreateDocumentRequestDTO;
import com.frederic.clienttra.dto.read.DocumentDTO;
import com.frederic.clienttra.dto.read.DocumentForListDTO;
import com.frederic.clienttra.dto.update.UpdateDocumentRequestDTO;
import com.frederic.clienttra.enums.DocumentStatus;
import com.frederic.clienttra.enums.DocumentType;
import com.frederic.clienttra.services.CustomerInvoiceService;
import com.frederic.clienttra.services.QuoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quotes")
@RequiredArgsConstructor
public class QuoteController {

    private final QuoteService documentService;
    private static final DocumentType DOC_TYPE = DocumentType.QUOTE;

    @GetMapping
    public ResponseEntity<List<DocumentForListDTO>> getAllQuotes() {
        List<DocumentForListDTO> result = documentService.getAllDocumentsByType(DOC_TYPE);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/by-id/{idDocument}")
    public ResponseEntity<DocumentDTO> getQuoteById(@PathVariable Integer idDocument){
        DocumentDTO dto = documentService.getDocumentById(DOC_TYPE, idDocument);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/by-customer/{idCompany}")
    public ResponseEntity<List<DocumentForListDTO>> getQuotesByCustomer(@PathVariable Integer idCompany) {
        List<DocumentForListDTO> result = documentService.getDocumentsByCompanyId(DOC_TYPE, idCompany);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/by-status")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<List<DocumentForListDTO>> getQuotesByStatus(@RequestParam DocumentStatus status) {
        List<DocumentForListDTO> result = documentService.getDocumentsByStatus(DOC_TYPE, status);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/by-customer/{idCompany}/by-status")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<List<DocumentForListDTO>> getQuotesByCustomerAndStatus(
            @PathVariable Integer idCompany,
            @RequestParam DocumentStatus status) {
        List<DocumentForListDTO> result = documentService.getDocumentsByIdCompanyAndStatus(DOC_TYPE, idCompany, status);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/create/{idCompany}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<DocumentDTO> createQuote(@PathVariable Integer idCompany,
                                                             @Valid @RequestBody CreateDocumentRequestDTO dto) {
        DocumentDTO created = documentService.createDocument(idCompany, dto, DOC_TYPE);
        return ResponseEntity.ok(created);
    }

    @PatchMapping("{idDocument}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<DocumentDTO> updateQuote(@PathVariable Integer idDocument, @RequestBody UpdateDocumentRequestDTO dto){

        DocumentDTO modified = documentService.updateDocument(idDocument,dto,DOC_TYPE);

        return ResponseEntity.ok(modified);
    }

    @GetMapping("/last-number")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<String> getLastQuoteNumber() {
        String lastNumber = documentService.getLastDocumentNumber(DOC_TYPE);
        return ResponseEntity.ok(lastNumber);
    }

    @DeleteMapping("/delete/{idDocument}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<GenericResponseDTO> deleteQuote(@PathVariable Integer idDocument){
        documentService.softDeleteDocument(idDocument);

        return ResponseEntity.ok(new GenericResponseDTO("quote.deleted.success"));
    }
}
