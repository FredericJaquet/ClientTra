package com.frederic.clienttra.controllers;

import com.frederic.clienttra.dto.GenericResponseDTO;
import com.frederic.clienttra.dto.create.CreateDocumentRequestDTO;
import com.frederic.clienttra.dto.read.DocumentDTO;
import com.frederic.clienttra.dto.read.DocumentForListDTO;
import com.frederic.clienttra.enums.DocumentStatus;
import com.frederic.clienttra.enums.DocumentType;
import com.frederic.clienttra.services.CustomerInvoiceService;
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
    private final CustomerInvoiceService customerInvoiceService;
    private final DocumentUtils documentUtils;
    private static final DocumentType DOC_TYPE = DocumentType.INV_CUST;

    @GetMapping
    public ResponseEntity<List<DocumentForListDTO>> getAllCustomerInvoices() {
        List<DocumentForListDTO> result = documentService.getAllDocumentsByType(DOC_TYPE);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{idDocument}")
    public ResponseEntity<DocumentDTO> getCustomerInvoiceById(@PathVariable Integer idDocument){
        DocumentDTO dto = customerInvoiceService.getDocumentById(DOC_TYPE, idDocument);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/by-customer/{idCompany}")
    public ResponseEntity<List<DocumentForListDTO>> getCustomerInvoicesByCustomer(@PathVariable Integer idCompany) {
        List<DocumentForListDTO> result = documentService.getDocumentsByCompanyId(DOC_TYPE, idCompany);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/by-status")
    public ResponseEntity<List<DocumentForListDTO>> getCustomerInvoicesByStatus(@RequestParam DocumentStatus status) {
        List<DocumentForListDTO> result = documentService.getDocumentsByStatus(DOC_TYPE, status);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/by-customer/{idCompany}/by-status")
    public ResponseEntity<List<DocumentForListDTO>> getCustomerInvoicesByCustomerAndStatus(
            @PathVariable Integer idCompany,
            @RequestParam DocumentStatus status) {
        List<DocumentForListDTO> result = documentService.getDocumentsByIdCompanyAndStatus(DOC_TYPE, idCompany, status);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/create/{idCompany}")
    public ResponseEntity<DocumentDTO> createCustomerInvoice(@PathVariable Integer idCompany,
                                                             @RequestBody CreateDocumentRequestDTO dto) {
        DocumentDTO created = documentService.createDocument(idCompany, dto, DOC_TYPE);
        return ResponseEntity.ok(created);
    }

    @PostMapping("modify-to-new-version/{idDocument}")//USo POST porque aquí no actualizamos, sino que creamos un doc nuevo
    public ResponseEntity<DocumentDTO> modifyDocumentToCustomerInvoice(@PathVariable Integer idDocument, @RequestBody CreateDocumentRequestDTO dto){

        DocumentDTO modified = documentService.updateDocument(idDocument,dto);

        return ResponseEntity.ok(modified);
    }

    @GetMapping("/last-number")
    public ResponseEntity<String> getLastCustomerInvoiceNumber() {
        String lastNumber = documentService.getLastDocumentNumber(DOC_TYPE);
        return ResponseEntity.ok(lastNumber);
    }

    @DeleteMapping("/delete/{idDocument}")
    public ResponseEntity<GenericResponseDTO> deleteCustomerInvoice(@PathVariable Integer idDocument){
        documentService.deleteDocumentSoft(idDocument);

        return ResponseEntity.ok(new GenericResponseDTO("invoice.deleted.success"));
    }
}