package com.frederic.clienttra.controllers;

import com.frederic.clienttra.dto.GenericResponseDTO;
import com.frederic.clienttra.dto.create.CreateDocumentRequestDTO;
import com.frederic.clienttra.dto.read.DocumentDTO;
import com.frederic.clienttra.dto.read.DocumentForListDTO;
import com.frederic.clienttra.enums.DocumentStatus;
import com.frederic.clienttra.enums.DocumentType;
import com.frederic.clienttra.services.CustomerInvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing customer invoices.
 * <p>
 * Supports listing, retrieval, creation, modification, and soft deletion
 * of customer invoices, scoped by document type {@link DocumentType#INV_CUST}.
 * Some operations are restricted to users with ADMIN or ACCOUNTING roles.
 */
@RestController
@RequestMapping("/api/customer-invoices")
@RequiredArgsConstructor
public class CustomerInvoiceController {

    private final CustomerInvoiceService documentService;

    // Constant DocumentType for customer invoices
    private static final DocumentType DOC_TYPE = DocumentType.INV_CUST;

    /**
     * Retrieves all customer invoices.
     *
     * @return a list of {@link DocumentForListDTO}
     */
    @GetMapping
    public ResponseEntity<List<DocumentForListDTO>> getAllCustomerInvoices() {
        List<DocumentForListDTO> result = documentService.getAllDocumentsByType(DOC_TYPE);
        return ResponseEntity.ok(result);
    }

    /**
     * Retrieves a customer invoice by its document ID.
     *
     * @param idDocument the ID of the document
     * @return the detailed {@link DocumentDTO}
     */
    @GetMapping("/by-id/{idDocument}")
    public ResponseEntity<DocumentDTO> getCustomerInvoiceById(@PathVariable Integer idDocument) {
        DocumentDTO dto = documentService.getDocumentById(DOC_TYPE, idDocument);
        return ResponseEntity.ok(dto);
    }

    /**
     * Retrieves all customer invoices linked to a specific customer company.
     *
     * @param idCompany the ID of the customer company
     * @return a list of {@link DocumentForListDTO}
     */
    @GetMapping("/by-customer/{idCompany}")
    public ResponseEntity<List<DocumentForListDTO>> getCustomerInvoicesByCustomer(@PathVariable Integer idCompany) {
        List<DocumentForListDTO> result = documentService.getDocumentsByCompanyId(DOC_TYPE, idCompany);
        return ResponseEntity.ok(result);
    }

    /**
     * Retrieves customer invoices filtered by document status.
     * Restricted to ADMIN and ACCOUNTING roles.
     *
     * @param status the document status filter
     * @return a list of {@link DocumentForListDTO}
     */
    @GetMapping("/by-status")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<List<DocumentForListDTO>> getCustomerInvoicesByStatus(@RequestParam DocumentStatus status) {
        List<DocumentForListDTO> result = documentService.getDocumentsByStatus(DOC_TYPE, status);
        return ResponseEntity.ok(result);
    }

    /**
     * Retrieves customer invoices filtered by customer company and status.
     * Restricted to ADMIN and ACCOUNTING roles.
     *
     * @param idCompany the ID of the customer company
     * @param status    the document status filter
     * @return a list of {@link DocumentForListDTO}
     */
    @GetMapping("/by-customer/{idCompany}/by-status")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<List<DocumentForListDTO>> getCustomerInvoicesByCustomerAndStatus(
            @PathVariable Integer idCompany,
            @RequestParam DocumentStatus status) {
        List<DocumentForListDTO> result = documentService.getDocumentsByIdCompanyAndStatus(DOC_TYPE, idCompany, status);
        return ResponseEntity.ok(result);
    }

    /**
     * Creates a new customer invoice linked to the specified customer company.
     * Restricted to ADMIN and ACCOUNTING roles.
     *
     * @param idCompany the ID of the customer company
     * @param dto       the creation request payload
     * @return the created {@link DocumentDTO}
     */
    @PostMapping("/create/{idCompany}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<DocumentDTO> createCustomerInvoice(@PathVariable Integer idCompany,
                                                             @Valid @RequestBody CreateDocumentRequestDTO dto) {
        DocumentDTO created = documentService.createDocument(idCompany, dto, DOC_TYPE);
        return ResponseEntity.ok(created);
    }

    /**
     * Creates a new version of an existing customer invoice.
     * Uses POST because it creates a new document instead of updating the existing one.
     * Restricted to ADMIN and ACCOUNTING roles.
     *
     * @param idDocument the ID of the existing document to modify
     * @param dto        the creation request payload for the new version
     * @return the modified {@link DocumentDTO}
     */
    @PostMapping("modify-to-new-version/{idDocument}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<DocumentDTO> modifyDocumentToCustomerInvoice(@PathVariable Integer idDocument, @RequestBody CreateDocumentRequestDTO dto) {
        DocumentDTO modified = documentService.updateDocument(idDocument, dto, DOC_TYPE);
        return ResponseEntity.ok(modified);
    }

    /**
     * Retrieves the last customer invoice number issued.
     * Restricted to ADMIN and ACCOUNTING roles.
     *
     * @return the last invoice number as a String
     */
    @GetMapping("/last-number")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<String> getLastCustomerInvoiceNumber() {
        String lastNumber = documentService.getLastDocumentNumber(DOC_TYPE);
        return ResponseEntity.ok(lastNumber);
    }

    /**
     * Soft deletes a customer invoice by its document ID.
     * Restricted to ADMIN and ACCOUNTING roles.
     *
     * @param idDocument the ID of the document to delete
     * @return a success message wrapped in {@link GenericResponseDTO}
     */
    @DeleteMapping("/delete/{idDocument}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<GenericResponseDTO> deleteCustomerInvoice(@PathVariable Integer idDocument) {
        documentService.softDeleteDocument(idDocument);
        return ResponseEntity.ok(new GenericResponseDTO("invoice.deleted.success"));
    }
}
