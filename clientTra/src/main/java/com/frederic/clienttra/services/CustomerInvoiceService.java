package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.create.CreateDocumentRequestDTO;
import com.frederic.clienttra.dto.read.DocumentDTO;
import com.frederic.clienttra.dto.read.DocumentForListDTO;
import com.frederic.clienttra.enums.DocumentStatus;
import com.frederic.clienttra.enums.DocumentType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerInvoiceService implements DocumentService{
    @Override
    public List<DocumentForListDTO> getAllDocumentsByType(DocumentType DOC_TYPE) {
        return List.of();
    }

    @Override
    public List<DocumentForListDTO> getDocumentsByCustomerId(DocumentType DOC_TYPE, Integer idCustomer) {
        return List.of();
    }

    @Override
    public List<DocumentForListDTO> getDocumentsByStatus(DocumentType DOC_TYPE, DocumentStatus status) {
        return List.of();
    }

    @Override
    public List<DocumentForListDTO> getDocumentsByCustomerAndStatus(DocumentType DOC_TYPE, Integer idCustomer, DocumentStatus status) {
        return List.of();
    }

    @Override
    public DocumentDTO createDocumentForCustomer(Integer idCompany, CreateDocumentRequestDTO dto, DocumentType DOC_TYPE) {
        return null;
    }

    @Override
    public String getLastDocumentNumber(DocumentType DOC_TYPE) {
        return "";
    }
}
