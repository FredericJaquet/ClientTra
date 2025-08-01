package com.frederic.clienttra.dto.read;

import com.frederic.clienttra.dto.bases.BaseSchemeDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for representing a billing or pricing scheme.
 * Contains general scheme information and the list of associated scheme lines.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchemeDTO implements BaseSchemeDTO {
    private Integer idScheme;
    private String schemeName;
    private Double price;
    private String units;
    private String fieldName;
    private String sourceLanguage;
    private String targetLanguage;
    private List<SchemeLineDTO> schemeLines;
}
