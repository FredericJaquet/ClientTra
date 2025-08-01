package com.frederic.clienttra.repositories;

import com.frederic.clienttra.entities.Document;
import com.frederic.clienttra.enums.DocumentStatus;
import com.frederic.clienttra.enums.DocumentType;
import com.frederic.clienttra.projections.InvoiceForPendingReportProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository interface for fetching invoices that are pending, intended for generating pending payment reports.
 * <p>
 * Extends JpaRepository to provide CRUD operations on Document entities.
 * Includes a custom query to fetch invoices filtered by status, owner company, and document type.
 */
public interface InvoiceForPendingReportRepository extends JpaRepository<Document, Integer> {

    /**
     * Retrieves a list of invoices matching the specified owner company, document type, and status.
     * The returned data includes minimal invoice information suitable for pending payment reports.
     *
     * @param idOwnerCompany the ID of the owning company
     * @param docType the type of document (typically INVOICE)
     * @param status the status of the document (e.g., PENDING)
     * @return list of invoice projections with summary data
     */
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
            @Param("status") DocumentStatus status);
}
