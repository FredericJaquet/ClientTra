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

@RestController
@RequestMapping("/api/customer-invoices")
@RequiredArgsConstructor
public class CustomerInvoiceController {

    private final CustomerInvoiceService documentService;
    private static final DocumentType DOC_TYPE = DocumentType.INV_CUST;

    @GetMapping
    public ResponseEntity<List<DocumentForListDTO>> getAllCustomerInvoices() {
        List<DocumentForListDTO> result = documentService.getAllDocumentsByType(DOC_TYPE);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/by-id/{idDocument}")
    public ResponseEntity<DocumentDTO> getCustomerInvoiceById(@PathVariable Integer idDocument){
        DocumentDTO dto = documentService.getDocumentById(DOC_TYPE, idDocument);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/by-customer/{idCompany}")
    public ResponseEntity<List<DocumentForListDTO>> getCustomerInvoicesByCustomer(@PathVariable Integer idCompany) {
        List<DocumentForListDTO> result = documentService.getDocumentsByCompanyId(DOC_TYPE, idCompany);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/by-status")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<List<DocumentForListDTO>> getCustomerInvoicesByStatus(@RequestParam DocumentStatus status) {
        List<DocumentForListDTO> result = documentService.getDocumentsByStatus(DOC_TYPE, status);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/by-customer/{idCompany}/by-status")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<List<DocumentForListDTO>> getCustomerInvoicesByCustomerAndStatus(
            @PathVariable Integer idCompany,
            @RequestParam DocumentStatus status) {
        List<DocumentForListDTO> result = documentService.getDocumentsByIdCompanyAndStatus(DOC_TYPE, idCompany, status);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/create/{idCompany}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<DocumentDTO> createCustomerInvoice(@PathVariable Integer idCompany,
                                                             @Valid @RequestBody CreateDocumentRequestDTO dto) {
        DocumentDTO created = documentService.createDocument(idCompany, dto, DOC_TYPE);
        return ResponseEntity.ok(created);
    }

    @PostMapping("modify-to-new-version/{idDocument}")//Uso POST porque aquí no actualizamos, sino que creamos un doc nuevo
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<DocumentDTO> modifyDocumentToCustomerInvoice(@PathVariable Integer idDocument, @RequestBody CreateDocumentRequestDTO dto){

        DocumentDTO modified = documentService.updateDocument(idDocument,dto, DOC_TYPE);

        return ResponseEntity.ok(modified);
    }

    @GetMapping("/last-number")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<String> getLastCustomerInvoiceNumber() {
        String lastNumber = documentService.getLastDocumentNumber(DOC_TYPE);
        return ResponseEntity.ok(lastNumber);
    }

    @DeleteMapping("/delete/{idDocument}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<GenericResponseDTO> deleteCustomerInvoice(@PathVariable Integer idDocument){
        documentService.softDeleteDocument(idDocument);

        return ResponseEntity.ok(new GenericResponseDTO("invoice.deleted.success"));
    }
}