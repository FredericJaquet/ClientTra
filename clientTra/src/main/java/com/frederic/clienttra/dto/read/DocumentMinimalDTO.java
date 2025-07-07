package com.frederic.clienttra.dto.read;

import lombok.*;

import java.time.LocalDate;

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
