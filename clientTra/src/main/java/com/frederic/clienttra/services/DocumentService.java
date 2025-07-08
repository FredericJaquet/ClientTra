package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.create.CreateDocumentRequestDTO;
import com.frederic.clienttra.dto.read.DocumentDTO;
import com.frederic.clienttra.dto.read.DocumentForListDTO;
import com.frederic.clienttra.enums.DocumentStatus;
import com.frederic.clienttra.enums.DocumentType;

import java.util.List;

public interface DocumentService {
    List<DocumentForListDTO> getAllDocumentsByType(DocumentType DOC_TYPE);
    List<DocumentForListDTO> getDocumentsByCustomerId(DocumentType DOC_TYPE, Integer idCustomer);
    List<DocumentForListDTO> getDocumentsByStatus(DocumentType DOC_TYPE, DocumentStatus status);
    List<DocumentForListDTO> getDocumentsByCustomerAndStatus(DocumentType DOC_TYPE, Integer idCustomer, DocumentStatus status);
    DocumentDTO createDocumentForCustomer(Integer idCompany, CreateDocumentRequestDTO dto, DocumentType DOC_TYPE);
    String getLastDocumentNumber(DocumentType DOC_TYPE);
}
