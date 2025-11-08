package com.frederic.clienttra.repositories;

import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.entities.Document;
import com.frederic.clienttra.enums.DocumentStatus;
import com.frederic.clienttra.enums.DocumentType;
import com.frederic.clienttra.projections.DocumentListProjection;
import com.frederic.clienttra.projections.DocumentMinimalProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Document} entities.
 * <p>
 * Provides CRUD operations and various custom queries to retrieve documents filtered by
 * document type, company, status, and owner company. Also supports projections for list views.
 */
public interface DocumentRepository extends JpaRepository<Document, Integer> {

    /**
     * Finds all documents owned by a specific company.
     *
     * @param ownerCompany the owning company
     * @return list of documents
     */
    List<Document> findAllByOwnerCompany(Company ownerCompany);

    /**
     * Retrieves a list of document summaries filtered by document type, associated company,
     * and owner company. Excludes documents with status 'MODIFIED' or 'DELETED'.
     * Results are ordered by document date and number descending.
     *
     * @param docType the document type to filter
     * @param idCompany the associated company ID
     * @param ownerCompany the owning company
     * @return list of document list projections
     */
    @Query("""
        SELECT
            d.idDocument AS idDocument,
            c.comName AS comName,
            d.docNumber AS docNumber,
            d.docDate AS docDate,
            d.totalNet AS totalNet,
            d.currency AS currency,
            d.status AS status,
            d.docType AS docType
        FROM Document d
        JOIN d.company c
        WHERE d.ownerCompany = :ownerCompany
          AND d.docType = :docType
          AND c.idCompany = :idCompany
          AND d.status NOT IN ('MODIFIED','DELETED')
        ORDER BY d.docDate DESC, d.docNumber DESC
    """)
    List<DocumentListProjection> findListByDocTypeIdCompanyAndOwnerCompany(@Param("docType") DocumentType docType,
                                                                           @Param("idCompany") Integer idCompany,
                                                                           @Param("ownerCompany") Company ownerCompany);

    /**
     * Retrieves documents filtered by document type, status, and owner company.
     * Excludes 'MODIFIED' and 'DELETED' statuses.
     * Ordered by document date and number descending.
     *
     * @param docType the document type to filter
     * @param status the document status to filter
     * @param ownerCompany the owning company
     * @return list of document list projections
     */
    @Query("""
        SELECT
            d.idDocument AS idDocument,
            c.comName AS comName,
            d.docNumber AS docNumber,
            d.docDate AS docDate,
            d.totalNet AS totalNet,
            d.currency AS currency,
            d.docType AS docType,
            d.status AS status
        FROM Document d
        JOIN d.company c
        WHERE d.ownerCompany = :ownerCompany
          AND d.docType = :docType
          AND d.status = :status
          AND d.status NOT IN ('MODIFIED','DELETED')
        ORDER BY d.docDate DESC, d.docNumber DESC
    """)
    List<DocumentListProjection> findListByDocTypeStatusAndOwnerCompany(@Param("docType") DocumentType docType,
                                                                        @Param("status") DocumentStatus status,
                                                                        @Param("ownerCompany") Company ownerCompany);

    /**
     * Retrieves documents filtered by document type and owner company,
     * excluding those with 'MODIFIED' or 'DELETED' status.
     * Ordered by document date and number descending.
     *
     * @param docType the document type to filter
     * @param ownerCompany the owning company
     * @return list of document list projections
     */
    @Query("""
        SELECT
            d.idDocument AS idDocument,
            c.comName AS comName,
            d.docNumber AS docNumber,
            d.docDate AS docDate,
            d.totalNet AS totalNet,
            d.currency AS currency,
            d.status AS status,
            d.docType AS docType
        FROM Document d
        JOIN d.company c
        WHERE d.ownerCompany = :ownerCompany
          AND d.docType = :docType
          AND d.status NOT IN ('MODIFIED','DELETED')
        ORDER BY d.docDate DESC, d.docNumber DESC
    """)
    List<DocumentListProjection> findListByDocTypeAndOwnerCompany(@Param("docType") DocumentType docType,
                                                                  @Param("ownerCompany") Company ownerCompany);

    /**
     * Retrieves documents filtered by document type, company ID, status, and owner company,
     * excluding 'MODIFIED' and 'DELETED' statuses.
     * Ordered by document date and number descending.
     *
     * @param docType the document type to filter
     * @param idCompany the associated company ID
     * @param status the document status to filter
     * @param ownerCompany the owning company
     * @return list of document list projections
     */
    @Query("""
        SELECT
            d.idDocument AS idDocument,
            c.comName AS comName,
            d.docNumber AS docNumber,
            d.docDate AS docDate,
            d.totalNet AS totalNet,
            d.currency AS currency,
            d.status AS status,
            d.docType AS docType
        FROM Document d
        JOIN d.company c
        WHERE d.ownerCompany = :ownerCompany
          AND d.docType = :docType
          AND d.status = :status
          AND c.idCompany = :idCompany
          AND d.status NOT IN ('MODIFIED','DELETED')
        ORDER BY d.docDate DESC, d.docNumber DESC
    """)
    List<DocumentListProjection> findListByDocTypeIdCompanyStatusAndOwnerCompany(@Param("docType") DocumentType docType,
                                                                                 @Param("idCompany") Integer idCompany,
                                                                                 @Param("status") DocumentStatus status,
                                                                                 @Param("ownerCompany") Company ownerCompany);
    /**
     * Finds a document by owner company, document ID, and document type.
     *
     * @param ownerCompany the owning company
     * @param idDocument the document ID
     * @param docType the document type
     * @return an Optional with the document if found
     */
    Optional<Document> findByOwnerCompanyAndIdDocumentAndDocType(Company ownerCompany, Integer idDocument, DocumentType docType);

    /**
     * Finds a document by owner company and document ID.
     *
     * @param ownerCompany the owning company
     * @param idDocument the document ID
     * @return an Optional with the document if found
     */
    Optional<Document> findByOwnerCompanyAndIdDocument(Company ownerCompany, Integer idDocument);

    /**
     * Finds the latest document number for a given owner company and document type.
     * Useful for generating sequential document numbers.
     *
     * @param ownerCompany the owning company
     * @param docType the document type
     * @return an Optional containing the highest document number as String, if any
     */
    @Query("""
    SELECT d.docNumber
        FROM Document d
        WHERE d.ownerCompany = :ownerCompany
          AND d.docType = :docType
          AND d.docNumber LIKE CONCAT(:yearPrefix, '%')
          AND d.status NOT IN ('MODIFIED','DELETED')
        ORDER BY d.docNumber DESC
    """)
    List<String> findDocNumbersByOwnerCompanyAndDocTypeAndDocNumberStartingWith(
            @Param("ownerCompany") Company ownerCompany,
            @Param("docType") DocumentType docType,
            @Param("yearPrefix") String yearPrefix,
            Pageable pageable);

    /**
     * Retrieves a minimal list of documents (ID and number) filtered by owner company and document type,
     * excluding documents with 'MODIFIED' or 'DELETED' status.
     * Ordered by document number descending.
     *
     * @param ownerCompany the owning company
     * @param docType the document type (as String)
     * @return list of minimal document projections
     */
    @Query("""
        SELECT
            d.idDocument AS idDocument,
            d.docNumber AS docNumber
        FROM Document d
        WHERE d.ownerCompany = :ownerCompany
          AND d.docType = :docType
          AND d.status NOT IN ('MODIFIED','DELETED')
        ORDER BY d.docNumber DESC
    """)
    List<DocumentMinimalProjection> findMinimalListByOwnerCompanyAndDocType(@Param("ownerCompany") Company ownerCompany, @Param("docType") String docType);

}
