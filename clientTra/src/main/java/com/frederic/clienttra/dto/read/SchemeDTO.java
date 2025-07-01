package com.frederic.clienttra.dto.read;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchemeDTO {
    private String schemeName;
    private double price;
    private String units;
    private String fieldName;
    private String sourceLanguage;
    private String targetLanguage;
    private Set<SchemeLineDTO> schemeLines;
}
