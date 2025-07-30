package com.frederic.clienttra.controllers;

import com.frederic.clienttra.dto.GenericResponseDTO;
import com.frederic.clienttra.dto.create.CreateDocumentRequestDTO;
import com.frederic.clienttra.dto.read.DocumentDTO;
import com.frederic.clienttra.dto.read.DocumentForListDTO;
import com.frederic.clienttra.enums.DocumentStatus;
import com.frederic.clienttra.enums.DocumentType;
import com.frederic.clienttra.services.ProviderInvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing provider invoices (purchase invoices).
 * <p>
 * Supports CRUD operations and filtering by status and provider.
 * Modification operations require ADMIN or ACCOUNTING roles.
 */
@RestController
@RequestMapping("/api/provider-invoices")
@RequiredArgsConstructor
public class ProviderInvoiceController {
    private final ProviderInvoiceService documentService;
    private static final DocumentType DOC_TYPE = DocumentType.INV_PROV;

    /**
     * Retrieves all provider invoices.
     *
     * @return list of {@link DocumentForListDTO}
     */
    @GetMapping
    public ResponseEntity<List<DocumentForListDTO>> getAllProviderInvoices() {
        List<DocumentForListDTO> result = documentService.getAllDocumentsByType(DOC_TYPE);
        return ResponseEntity.ok(result);
    }

    /**
     * Retrieves a provider invoice by its ID.
     *
     * @param idDocument document ID
     * @return the {@link DocumentDTO} with full details
     */
    @GetMapping("/by-id/{idDocument}")
    public ResponseEntity<DocumentDTO> getProviderInvoiceById(@PathVariable Integer idDocument){
        DocumentDTO dto = documentService.getDocumentById(DOC_TYPE, idDocument);
        return ResponseEntity.ok(dto);
    }

    /**
     * Retrieves provider invoices by provider company ID.
     *
     * @param idCompany provider company ID
     * @return list of {@link DocumentForListDTO} for the provider
     */
    @GetMapping("/by-customer/{idCompany}")
    public ResponseEntity<List<DocumentForListDTO>> getProviderInvoicesByProvider(@PathVariable Integer idCompany) {
        List<DocumentForListDTO> result = documentService.getDocumentsByCompanyId(DOC_TYPE, idCompany);
        return ResponseEntity.ok(result);
    }

    /**
     * Retrieves provider invoices filtered by status.
     * Requires ADMIN or ACCOUNTING roles.
     *
     * @param status the document status to filter
     * @return filtered list of provider invoices
     */
    @GetMapping("/by-status")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<List<DocumentForListDTO>> getProviderInvoicesByStatus(@RequestParam DocumentStatus status) {
        List<DocumentForListDTO> result = documentService.getDocumentsByStatus(DOC_TYPE, status);
        return ResponseEntity.ok(result);
    }

    /**
     * Retrieves provider invoices filtered by provider and status.
     * Requires ADMIN or ACCOUNTING roles.
     *
     * @param idCompany provider company ID
     * @param status    document status to filter
     * @return filtered list of provider invoices for the provider
     */
    @GetMapping("/by-customer/{idCompany}/by-status")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<List<DocumentForListDTO>> getProviderInvoicesByProviderAndStatus(
            @PathVariable Integer idCompany,
            @RequestParam DocumentStatus status) {
        List<DocumentForListDTO> result = documentService.getDocumentsByIdCompanyAndStatus(DOC_TYPE, idCompany, status);
        return ResponseEntity.ok(result);
    }

    /**
     * Creates a new provider invoice for the specified provider company.
     * Requires ADMIN or ACCOUNTING roles.
     *
     * @param idCompany provider company ID
     * @param dto       creation data transfer object
     * @return the created {@link DocumentDTO}
     */
    @PostMapping("/create/{idCompany}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<DocumentDTO> createProviderInvoice(@PathVariable Integer idCompany,
                                                             @Valid @RequestBody CreateDocumentRequestDTO dto) {
        DocumentDTO created = documentService.createDocument(idCompany, dto, DOC_TYPE);
        return ResponseEntity.ok(created);
    }

    /**
     * Modifies an existing provider invoice by creating a new version.
     * Uses POST instead of PATCH because it creates a new document version.
     * Requires ADMIN or ACCOUNTING roles.
     *
     * @param idDocument ID of the document to modify
     * @param dto        new document data
     * @return the modified {@link DocumentDTO}
     */
    @PostMapping("modify-to-new-version/{idDocument}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<DocumentDTO> modifyDocumentToProviderInvoice(@PathVariable Integer idDocument, @RequestBody CreateDocumentRequestDTO dto) {
        DocumentDTO modified = documentService.updateDocument(idDocument, dto, DOC_TYPE);
        return ResponseEntity.ok(modified);
    }

    /**
     * Soft deletes a provider invoice.
     * Requires ADMIN or ACCOUNTING roles.
     *
     * @param idDocument document ID to soft delete
     * @return success message wrapped in {@link GenericResponseDTO}
     */
    @DeleteMapping("/delete/{idDocument}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<GenericResponseDTO> deleteProviderInvoice(@PathVariable Integer idDocument){
        documentService.softDeleteDocument(idDocument);
        return ResponseEntity.ok(new GenericResponseDTO("invoice.deleted.success"));
    }

}
