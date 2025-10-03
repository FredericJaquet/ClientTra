package com.frederic.clienttra.repositories;

import com.frederic.clienttra.entities.Document;
import com.frederic.clienttra.enums.DocumentType;
import com.frederic.clienttra.projections.InvoiceForCashFlowGraphProjection;
import com.frederic.clienttra.projections.InvoiceForCashFlowReportProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for retrieving invoice data specifically for cash flow reports.
 * <p>
 * Extends JpaRepository for basic CRUD operations on Document entities.
 * Provides a custom query to fetch detailed invoice data within a date range,
 * filtered by the owner company and document type.
 */
public interface InvoiceForCashFlowReportRepository extends JpaRepository<Document, Integer> {

    /**
     * Finds invoices for cash flow reporting purposes, filtered by date range, owner company ID,
     * and document type.
     *
     * @param initDate the start date of the range (inclusive)
     * @param endDate the end date of the range (inclusive)
     * @param idOwnerCompany the ID of the owning company
     * @param docType the type of document (usually INVOICE)
     * @return list of projections containing invoice details for the report
     */
    @Query("""
        SELECT
            c.id AS idCompany,
            c.legalName AS legalName,
            c.vatNumber AS vatNumber,
            d.docNumber AS invoiceNumber,
            d.docDate AS docDate,
            d.totalNet AS totalNet,
            d.totalVat AS totalVat,
            d.totalWithholding AS totalWithholding
        FROM Document d
        JOIN d.company c
        WHERE d.docDate BETWEEN :initDate AND :endDate
          AND d.ownerCompany.idCompany = :idOwnerCompany
          AND d.docType = :docType
          AND d.status NOT IN (com.frederic.clienttra.enums.DocumentStatus.MODIFIED,
                               com.frederic.clienttra.enums.DocumentStatus.DELETED)
    """)
    List<InvoiceForCashFlowReportProjection> findInvoicesForCashFlowReport(
            @Param("initDate") LocalDate initDate,
            @Param("endDate") LocalDate endDate,
            @Param("idOwnerCompany") Integer idOwnerCompany,
            @Param("docType") DocumentType docType);

    /**
     * Finds invoices for cash flow Graph purposes, filtered by date range, owner company ID,
     * and document type.
     *
     * @param initDate the start date of the range (inclusive)
     * @param endDate the end date of the range (inclusive)
     * @param idOwnerCompany the ID of the owning company
     * @param docType the type of document (usually INVOICE)
     * @return list of projections containing invoice details for the graph
     */
    @Query("""
        SELECT
            d.docDate AS docDate,
            d.totalNet AS totalNet
        FROM Document d
        WHERE d.docDate BETWEEN :initDate AND :endDate
          AND d.ownerCompany.idCompany = :idOwnerCompany
          AND d.docType = :docType
          AND d.status NOT IN (com.frederic.clienttra.enums.DocumentStatus.MODIFIED,
                               com.frederic.clienttra.enums.DocumentStatus.DELETED)
    """)
    List<InvoiceForCashFlowGraphProjection> findInvoicesForCashFlowGraph(
            @Param("initDate") LocalDate initDate,
            @Param("endDate") LocalDate endDate,
            @Param("idOwnerCompany") Integer idOwnerCompany,
            @Param("docType") DocumentType docType);
}
