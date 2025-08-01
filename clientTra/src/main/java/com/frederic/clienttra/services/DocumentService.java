package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.bases.BaseDocumentDTO;
import com.frederic.clienttra.dto.create.CreateDocumentRequestDTO;
import com.frederic.clienttra.dto.read.DocumentDTO;
import com.frederic.clienttra.dto.read.DocumentForListDTO;
import com.frederic.clienttra.enums.DocumentStatus;
import com.frederic.clienttra.enums.DocumentType;

import java.util.List;

/**
 * Service interface for managing documents of various types.
 */
public interface DocumentService {

    /**
     * Retrieves all documents filtered by their document type.
     *
     * @param type the type of documents to retrieve
     * @return a list of document summaries matching the type
     */
    List<DocumentForListDTO> getAllDocumentsByType(DocumentType type);

    /**
     * Retrieves documents for a specific company filtered by document type.
     *
     * @param type      the document type
     * @param idCompany the ID of the company
     * @return a list of document summaries belonging to the company and type
     */
    List<DocumentForListDTO> getDocumentsByCompanyId(DocumentType type, Integer idCompany);

    /**
     * Retrieves documents filtered by document type and their current status.
     *
     * @param type   the document type
     * @param status the status to filter by (e.g. PENDING, DELETED)
     * @return a list of document summaries matching the criteria
     */
    List<DocumentForListDTO> getDocumentsByStatus(DocumentType type, DocumentStatus status);

    /**
     * Retrieves documents filtered by company ID, document type, and status.
     *
     * @param type      the document type
     * @param idCompany the ID of the company
     * @param status    the document status to filter by
     * @return a list of document summaries matching the criteria
     */
    List<DocumentForListDTO> getDocumentsByIdCompanyAndStatus(DocumentType type, Integer idCompany, DocumentStatus status);

    /**
     * Creates a new document for a given company.
     *
     * @param idCompany the ID of the company to which the document belongs
     * @param dto       the base document data transfer object containing document details
     * @param type      the type of document to create
     * @return the created document as a DTO
     */
    DocumentDTO createDocument(Integer idCompany, BaseDocumentDTO dto, DocumentType type);

    /**
     * Updates an existing document identified by its ID.
     *
     * @param idDocument the ID of the document to update
     * @param dto        the base document data transfer object with updated fields
     * @param type       the type of the document being updated
     * @return the updated document as a DTO
     */
    DocumentDTO updateDocument(Integer idDocument, BaseDocumentDTO dto, DocumentType type);

    /**
     * Retrieves a document by its ID and document type.
     *
     * @param type the type of the document
     * @param id   the unique identifier of the document
     * @return the document details as a DTO
     */
    DocumentDTO getDocumentById(DocumentType type, Integer id);

    /**
     * Performs a soft delete on the document identified by its ID.
     * This usually means changing the document status to indicate deletion,
     * without physically removing it from the database.
     *
     * @param id the ID of the document to soft delete
     */
    void softDeleteDocument(Integer id);
}
