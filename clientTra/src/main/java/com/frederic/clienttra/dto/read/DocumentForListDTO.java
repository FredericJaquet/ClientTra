package com.frederic.clienttra.dto.read;

import com.frederic.clienttra.enums.DocumentStatus;
import lombok.*;

import java.time.LocalDate;

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
}
