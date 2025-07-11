package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.bases.BaseDocumentDTO;
import com.frederic.clienttra.dto.create.CreateDocumentRequestDTO;
import com.frederic.clienttra.dto.read.DocumentDTO;
import com.frederic.clienttra.dto.read.DocumentForListDTO;
import com.frederic.clienttra.enums.DocumentStatus;
import com.frederic.clienttra.enums.DocumentType;

import java.util.List;

public interface DocumentService {
    List<DocumentForListDTO> getAllDocumentsByType(DocumentType type);
    List<DocumentForListDTO> getDocumentsByCompanyId(DocumentType type, Integer idCompany);
    List<DocumentForListDTO> getDocumentsByStatus(DocumentType type, DocumentStatus status);
    List<DocumentForListDTO> getDocumentsByIdCompanyAndStatus(DocumentType type, Integer idCompany, DocumentStatus status);
    DocumentDTO createDocument(Integer idCompany, BaseDocumentDTO dto, DocumentType type);
    DocumentDTO updateDocument(Integer idDocument, BaseDocumentDTO dto, DocumentType type);
    DocumentDTO getDocumentById(DocumentType type, Integer id);
    void softDeleteDocument(Integer id);

}
