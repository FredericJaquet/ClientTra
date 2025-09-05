package com.frederic.clienttra.controllers;

import com.frederic.clienttra.dto.GenericResponseDTO;
import com.frederic.clienttra.dto.create.CreateDocumentRequestDTO;
import com.frederic.clienttra.dto.read.DocumentDTO;
import com.frederic.clienttra.dto.read.DocumentForListDTO;
import com.frederic.clienttra.dto.update.UpdateDocumentRequestDTO;
import com.frederic.clienttra.enums.DocumentStatus;
import com.frederic.clienttra.enums.DocumentType;
import com.frederic.clienttra.services.QuoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing quote-type documents (QUOTE).
 * <p>
 * Provides endpoints for CRUD operations and filtered queries.
 * Modification and deletion require ADMIN or ACCOUNTING roles.
 */
@RestController
@RequestMapping("/api/quotes")
@RequiredArgsConstructor
public class QuoteController {

    private final QuoteService documentService;
    private static final DocumentType DOC_TYPE = DocumentType.QUOTE;

    /**
     * Retrieves all quotes.
     * @return list of summarized quotes
     */
    @GetMapping
    public ResponseEntity<List<DocumentForListDTO>> getAllQuotes() {
        List<DocumentForListDTO> result = documentService.getAllDocumentsByType(DOC_TYPE);
        return ResponseEntity.ok(result);
    }

    /**
     * Retrieves a quote by its ID.
     * @param idDocument quote ID
     * @return detailed quote
     */
    @GetMapping("/by-id/{idDocument}")
    public ResponseEntity<DocumentDTO> getQuoteById(@PathVariable Integer idDocument){
        DocumentDTO dto = documentService.getDocumentById(DOC_TYPE, idDocument);
        return ResponseEntity.ok(dto);
    }

    /**
     * Retrieves quotes associated with a customer company.
     * @param idCompany customer company ID
     * @return list of quotes for the company
     */
    @GetMapping("/by-customer/{idCompany}")
    public ResponseEntity<List<DocumentForListDTO>> getQuotesByCustomer(@PathVariable Integer idCompany) {
        List<DocumentForListDTO> result = documentService.getDocumentsByCompanyId(DOC_TYPE, idCompany);
        return ResponseEntity.ok(result);
    }

    /**
     * Retrieves quotes filtered by status.
     * Accessible only to ADMIN or ACCOUNTING roles.
     * @param status document status
     * @return filtered list of quotes
     */
    @GetMapping("/by-status")
    public ResponseEntity<List<DocumentForListDTO>> getQuotesByStatus(@RequestParam DocumentStatus status) {
        List<DocumentForListDTO> result = documentService.getDocumentsByStatus(DOC_TYPE, status);
        return ResponseEntity.ok(result);
    }

    /**
     * Retrieves quotes filtered by company and status.
     * Accessible only to ADMIN or ACCOUNTING roles.
     * @param idCompany customer company ID
     * @param status document status
     * @return list of quotes filtered by company and status
     */
    @GetMapping("/by-customer/{idCompany}/by-status")
    public ResponseEntity<List<DocumentForListDTO>> getQuotesByCustomerAndStatus(
            @PathVariable Integer idCompany,
            @RequestParam DocumentStatus status) {
        List<DocumentForListDTO> result = documentService.getDocumentsByIdCompanyAndStatus(DOC_TYPE, idCompany, status);
        return ResponseEntity.ok(result);
    }

    /**
     * Creates a new quote for a company.
     * Accessible only to ADMIN or ACCOUNTING roles.
     * @param idCompany customer company ID
     * @param dto new quote data
     * @return created quote with details
     */
    @PostMapping("/create/{idCompany}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<DocumentDTO> createQuote(@PathVariable Integer idCompany,
                                                   @Valid @RequestBody CreateDocumentRequestDTO dto) {
        DocumentDTO created = documentService.createDocument(idCompany, dto, DOC_TYPE);
        return ResponseEntity.ok(created);
    }

    /**
     * Updates an existing quote.
     * Accessible only to ADMIN or ACCOUNTING roles.
     * @param idDocument quote ID to update
     * @param dto data to update the quote
     * @return updated quote with details
     */
    @PatchMapping("{idDocument}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<DocumentDTO> updateQuote(@PathVariable Integer idDocument, @RequestBody UpdateDocumentRequestDTO dto){
        DocumentDTO modified = documentService.updateDocument(idDocument,dto,DOC_TYPE);
        return ResponseEntity.ok(modified);
    }

    /**
     * Retrieves the last quote number used.
     * Accessible only to ADMIN or ACCOUNTING roles.
     * @return last quote number as String
     */
    @GetMapping("/last-number")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<String> getLastQuoteNumber() {
        String lastNumber = documentService.getLastDocumentNumber(DOC_TYPE);
        return ResponseEntity.ok(lastNumber);
    }

    /**
     * Soft deletes a quote.
     * Accessible only to ADMIN or ACCOUNTING roles.
     * @param idDocument quote ID to delete
     * @return success message
     */
    @DeleteMapping("/delete/{idDocument}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<GenericResponseDTO> deleteQuote(@PathVariable Integer idDocument){
        documentService.softDeleteDocument(idDocument);
        return ResponseEntity.ok(new GenericResponseDTO("quote.deleted.success"));
    }
}
