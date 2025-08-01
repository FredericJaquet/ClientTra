package com.frederic.clienttra.dto.read;

import com.frederic.clienttra.enums.DocumentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for representing a summary of an invoice for pending reports.
 * Contains the document ID, company name, document number, total amount to pay,
 * and the status of the document.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceSummaryForPendingReportDTO {
    private Integer idDocument;
    private String comName;
    private String docNumber;
    private Double totalToPay;
    private DocumentStatus status;

}
