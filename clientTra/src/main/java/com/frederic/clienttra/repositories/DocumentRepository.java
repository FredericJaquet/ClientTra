package com.frederic.clienttra.repositories;

import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.entities.Document;
import com.frederic.clienttra.enums.DocumentStatus;
import com.frederic.clienttra.enums.DocumentType;
import com.frederic.clienttra.projections.DocumentListProjection;
import com.frederic.clienttra.projections.DocumentMinimalProjection;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Integer> {

    // 1. Lista con detalle para el frontend según tipo documento y empresa cliente/proveedor
    @Query("""
        SELECT
            d.idDocument AS idDocument,
            c.comName AS comName,
            d.docNumber AS docNumber,
            d.docDate AS docDate,
            d.totalNet AS totalNet,
            d.currency AS currency,
            d.docType AS docType
        FROM Document d
        JOIN d.company c
        WHERE d.ownerCompany = :ownerCompany
          AND d.docType = :docType
          AND c.idCompany = :idCompany
          AND d.status NOT IN ('MODIFIED')
        ORDER BY d.docDate DESC, d.docNumber DESC
    """)
    List<DocumentListProjection> findListByDocTypeIdCompanyAndOwnerCompany(@Param("docType") DocumentType docType,@Param("idCompany") Integer idCompany,@Param("ownerCompany") Company ownerCompany  );

    // 1. Lista con detalle para el frontend según tipo documento
    @Query("""
        SELECT
            d.idDocument AS idDocument,
            c.comName AS comName,
            d.docNumber AS docNumber,
            d.docDate AS docDate,
            d.totalNet AS totalNet,
            d.currency AS currency,
            d.docType AS docType
        FROM Document d
        JOIN d.company c
        WHERE d.ownerCompany = :ownerCompany
          AND d.docType = :docType
          AND d.status = :status
          AND d.status NOT IN ('MODIFIED')
        ORDER BY d.docDate DESC, d.docNumber DESC
    """)
    List<DocumentListProjection> findListByDocTypeStatusAndOwnerCompany(@Param("docType") DocumentType docType, @Param("status") DocumentStatus status, @Param("ownerCompany") Company ownerCompany  );

    // 1. Lista con detalle para el frontend según tipo documento
    @Query("""
        SELECT
            d.idDocument AS idDocument,
            c.comName AS comName,
            d.docNumber AS docNumber,
            d.docDate AS docDate,
            d.totalNet AS totalNet,
            d.currency AS currency,
            d.docType AS docType
        FROM Document d
        JOIN d.company c
        WHERE d.ownerCompany = :ownerCompany
          AND d.docType = :docType
          AND d.status NOT IN ('MODIFIED')
        ORDER BY d.docDate DESC, d.docNumber DESC
    """)
    List<DocumentListProjection> findListByDocTypeAndOwnerCompany(@Param("docType") DocumentType docType, @Param("ownerCompany") Company ownerCompany  );

    // 1. Lista con detalle para el frontend según tipo documento
    @Query("""
        SELECT
            d.idDocument AS idDocument,
            c.comName AS comName,
            d.docNumber AS docNumber,
            d.docDate AS docDate,
            d.totalNet AS totalNet,
            d.currency AS currency,
            d.docType AS docType
        FROM Document d
        JOIN d.company c
        WHERE d.ownerCompany = :ownerCompany
          AND d.docType = :docType
          AND d.status = :status
          AND c.idCompany = :idCompany
          AND d.status NOT IN ('MODIFIED')
        ORDER BY d.docDate DESC, d.docNumber DESC
    """)
    List<DocumentListProjection> findListByDocTypeIdCompanyStatusAndOwnerCompany(@Param("docType") DocumentType docType, @Param("idCompany") Integer idCompany, @Param("status") DocumentStatus status, @Param("ownerCompany") Company ownerCompany  );

    // 2. Buscar un documento específico por ownerCompany e idDocument
    Optional<Document> findByOwnerCompanyIdDocumentAndDocType(Company ownerCompany, Integer idDocument, DocumentType docType);

    // 2. Buscar un documento específico por ownerCompany e idDocument
    Optional<Document> findByOwnerCompanyAndIdDocument(Company ownerCompany, Integer idDocument);

    // 3. Buscar el último número de documento para secuencialidad, por ownerCompany y docType
    Optional<String> findTop1DocNumberByOwnerCompanyAndDocTypeOrderByDocNumberDesc(Company ownerCompany, DocumentType docType);


    // 4. Lista mínima para combos (id y docNumber) filtrada por ownerCompany y docType
    @Query("""
        SELECT
            d.idDocument AS idDocument,
            d.docNumber AS docNumber
        FROM Document d
        WHERE d.ownerCompany = :ownerCompany
          AND d.docType = :docType
          AND d.status NOT IN ('MODIFIED')
        ORDER BY d.docNumber DESC
    """)
    List<DocumentMinimalProjection> findMinimalListByOwnerCompanyAndDocType(@Param("ownerCompany") Company ownerCompany,@Param("docType") String docType);

}
