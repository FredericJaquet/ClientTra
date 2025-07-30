package com.frederic.clienttra.repositories;

import com.frederic.clienttra.entities.Document;
import com.frederic.clienttra.enums.DocumentType;
import com.frederic.clienttra.projections.InvoiceForCashFlowReportProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface InvoiceForCashFlowReportRepository extends JpaRepository<Document, Integer> {
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
""")
    List<InvoiceForCashFlowReportProjection> findInvoicesForReport(
            @Param("initDate") LocalDate initDate,
            @Param("endDate") LocalDate endDate,
            @Param("idOwnerCompany") Integer idOwnerCompany,
            @Param("docType") DocumentType docType);

}
