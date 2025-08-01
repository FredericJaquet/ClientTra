package com.frederic.clienttra.dto.read;

import lombok.*;

import java.time.LocalDate;

/**
 * Minimal DTO for a document.
 * Contains only the essential identifying information: document ID, document number, and document date.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentMinimalDTO {
    private Integer idDocument;
    private String docNumber;
    private LocalDate docDate;
}
