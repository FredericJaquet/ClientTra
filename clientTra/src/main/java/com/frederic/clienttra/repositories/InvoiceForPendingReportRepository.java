package com.frederic.clienttra.repositories;

import com.frederic.clienttra.entities.Document;
import com.frederic.clienttra.enums.DocumentStatus;
import com.frederic.clienttra.enums.DocumentType;
import com.frederic.clienttra.projections.InvoiceForPendingReportProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InvoiceForPendingReportRepository extends JpaRepository<Document, Integer> {
    @Query("""
            SELECT
                d.id AS idDocument,
                c.comName AS comName,
                d.docNumber AS docNumber,
                d.totalToPay AS totalToPay,
                d.status AS status,
                d.docDate AS docDate
            FROM Document d
            JOIN d.company c
            WHERE d.status = :status
            AND d.ownerCompany.idCompany = :idOwnerCompany
            AND d.docType = :docType
            """)
    List<InvoiceForPendingReportProjection> findInvoiceForPendingReport(
            @Param("idOwnerCompany") Integer idOwnerCompany,
            @Param("docType") DocumentType docType,
            @Param("status")DocumentStatus status);

}
