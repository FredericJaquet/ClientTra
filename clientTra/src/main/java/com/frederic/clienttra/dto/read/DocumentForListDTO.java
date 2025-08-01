package com.frederic.clienttra.dto.read;

import com.frederic.clienttra.enums.DocumentStatus;
import com.frederic.clienttra.enums.DocumentType;
import lombok.*;

import java.time.LocalDate;

/**
 * DTO for representing a summarized view of a document for listing purposes.
 * Includes document ID, company name, document number, date, net total, currency, status, and type.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentForListDTO {

    private Integer idDocument;
    private String comName;
    private String docNumber;
    private LocalDate docDate;
    private Double totalNet;
    private String currency;
    private DocumentStatus status;
    private DocumentType type;
}
