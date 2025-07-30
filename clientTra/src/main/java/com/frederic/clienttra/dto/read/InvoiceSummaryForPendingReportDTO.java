package com.frederic.clienttra.dto.read;

import com.frederic.clienttra.enums.DocumentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
