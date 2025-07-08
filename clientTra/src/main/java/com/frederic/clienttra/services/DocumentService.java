package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.create.CreateDocumentRequestDTO;
import com.frederic.clienttra.dto.read.DocumentDTO;
import com.frederic.clienttra.dto.read.DocumentForListDTO;
import com.frederic.clienttra.dto.update.UpdateDocumentRequestDTO;
import com.frederic.clienttra.enums.DocumentStatus;
import com.frederic.clienttra.enums.DocumentType;

import java.util.List;

public interface DocumentService {
    List<DocumentForListDTO> getAllDocumentsByType(DocumentType DOC_TYPE);
    List<DocumentForListDTO> getDocumentsByCompanyId(DocumentType DOC_TYPE, Integer idCustomer);
    List<DocumentForListDTO> getDocumentsByStatus(DocumentType DOC_TYPE, DocumentStatus status);
    List<DocumentForListDTO> getDocumentsByIdCompanyAndStatus(DocumentType DOC_TYPE, Integer idCompany, DocumentStatus status);
    DocumentDTO createDocument(Integer idCompany, CreateDocumentRequestDTO dto, DocumentType DOC_TYPE);
    void updateDocument(Integer idDocument, CreateDocumentRequestDTO dto);
    DocumentDTO getDocumentById(DocumentType DOC_TYPE, Integer id);
    String getLastDocumentNumber(DocumentType DOC_TYPE);

}
