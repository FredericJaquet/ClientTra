package com.frederic.clienttra.controllers;

import com.frederic.clienttra.dto.GenericResponseDTO;
import com.frederic.clienttra.dto.create.CreateDocumentRequestDTO;
import com.frederic.clienttra.dto.read.DocumentDTO;
import com.frederic.clienttra.dto.read.DocumentForListDTO;
import com.frederic.clienttra.dto.update.UpdateDocumentRequestDTO;
import com.frederic.clienttra.enums.DocumentStatus;
import com.frederic.clienttra.enums.DocumentType;
import com.frederic.clienttra.services.PurchaseOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing purchase orders (POs).
 * <p>
 * Supports retrieval, creation, updating, and soft deletion of purchase orders.
 * All modification endpoints require ADMIN or ACCOUNTING roles.
 */
@RestController
@RequestMapping("/api/pos")
@RequiredArgsConstructor
public class PurchaseOrderController {

    private final PurchaseOrderService documentService;
    private static final DocumentType DOC_TYPE = DocumentType.PO;

    /**
     * Get all purchase orders.
     *
     * @return list of purchase orders as {@link DocumentForListDTO}
     */
    @GetMapping
    public ResponseEntity<List<DocumentForListDTO>> getAllQuotes() {
        List<DocumentForListDTO> result = documentService.getAllDocumentsByType(DOC_TYPE);
        return ResponseEntity.ok(result);
    }

    /**
     * Get a purchase order by its ID.
     *
     * @param idDocument the purchase order ID
     * @return detailed purchase order DTO
     */
    @GetMapping("/by-id/{idDocument}")
    public ResponseEntity<DocumentDTO> getQuoteById(@PathVariable Integer idDocument){
        DocumentDTO dto = documentService.getDocumentById(DOC_TYPE, idDocument);
        return ResponseEntity.ok(dto);
    }

    /**
     * Get purchase orders by customer (provider) company ID.
     *
     * @param idCompany the company ID
     * @return list of purchase orders linked to the company
     */
    @GetMapping("/by-customer/{idCompany}")
    public ResponseEntity<List<DocumentForListDTO>> getQuotesByCustomer(@PathVariable Integer idCompany) {
        List<DocumentForListDTO> result = documentService.getDocumentsByCompanyId(DOC_TYPE, idCompany);
        return ResponseEntity.ok(result);
    }

    /**
     * Get purchase orders filtered by document status.
     * Requires ADMIN or ACCOUNTING roles.
     *
     * @param status the document status to filter
     * @return list of purchase orders matching status
     */
    @GetMapping("/by-status")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<List<DocumentForListDTO>> getQuotesByStatus(@RequestParam DocumentStatus status) {
        List<DocumentForListDTO> result = documentService.getDocumentsByStatus(DOC_TYPE, status);
        return ResponseEntity.ok(result);
    }

    /**
     * Get purchase orders filtered by company and status.
     * Requires ADMIN or ACCOUNTING roles.
     *
     * @param idCompany the company ID
     * @param status    document status to filter
     * @return list of purchase orders matching company and status
     */
    @GetMapping("/by-customer/{idCompany}/by-status")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<List<DocumentForListDTO>> getQuotesByCustomerAndStatus(
            @PathVariable Integer idCompany,
            @RequestParam DocumentStatus status) {
        List<DocumentForListDTO> result = documentService.getDocumentsByIdCompanyAndStatus(DOC_TYPE, idCompany, status);
        return ResponseEntity.ok(result);
    }

    /**
     * Create a new purchase order for a given company.
     * Requires ADMIN or ACCOUNTING roles.
     *
     * @param idCompany company ID
     * @param dto       DTO containing the purchase order data
     * @return the created purchase order DTO
     */
    @PostMapping("/create/{idCompany}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<DocumentDTO> createQuote(@PathVariable Integer idCompany,
                                                   @Valid @RequestBody CreateDocumentRequestDTO dto) {
        DocumentDTO created = documentService.createDocument(idCompany, dto, DOC_TYPE);
        return ResponseEntity.ok(created);
    }

    /**
     * Update an existing purchase order.
     * Requires ADMIN or ACCOUNTING roles.
     *
     * @param idDocument purchase order ID
     * @param dto        DTO containing update data
     * @return updated purchase order DTO
     */
    @PatchMapping("{idDocument}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<DocumentDTO> updateQuote(@PathVariable Integer idDocument, @RequestBody UpdateDocumentRequestDTO dto){
        DocumentDTO modified = documentService.updateDocument(idDocument,dto,DOC_TYPE);
        return ResponseEntity.ok(modified);
    }

    /**
     * Get the last purchase order number.
     * Requires ADMIN or ACCOUNTING roles.
     *
     * @return the last purchase order number as a String
     */
    @GetMapping("/last-number")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<String> getLastQuoteNumber() {
        String lastNumber = documentService.getLastDocumentNumber(DOC_TYPE);
        return ResponseEntity.ok(lastNumber);
    }

    /**
     * Soft delete a purchase order.
     * Requires ADMIN or ACCOUNTING roles.
     *
     * @param idDocument purchase order ID
     * @return success message wrapped in {@link GenericResponseDTO}
     */
    @DeleteMapping("/delete/{idDocument}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<GenericResponseDTO> deleteQuote(@PathVariable Integer idDocument){
        documentService.softDeleteDocument(idDocument);
        return ResponseEntity.ok(new GenericResponseDTO("quote.deleted.success"));
    }
}
