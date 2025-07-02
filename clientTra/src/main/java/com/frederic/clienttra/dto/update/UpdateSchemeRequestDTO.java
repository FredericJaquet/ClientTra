package com.frederic.clienttra.dto.update;

import com.frederic.clienttra.dto.bases.BaseSchemeDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateSchemeRequestDTO implements BaseSchemeDTO {
    private Integer idScheme;
    private String schemeName;
    private Double price;
    private String units;
    private String fieldName;
    private String sourceLanguage;
    private String targetLanguage;
    private List<UpdateSchemeLineRequestDTO> schemeLines;
}
