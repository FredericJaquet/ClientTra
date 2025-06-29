package com.frederic.clienttra.dto.read;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
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
