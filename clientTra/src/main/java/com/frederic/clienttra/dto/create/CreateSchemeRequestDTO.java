package com.frederic.clienttra.dto.create;

import com.frederic.clienttra.dto.bases.BaseSchemeDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO used to create a new pricing scheme.
 * Implements the BaseSchemeDTO interface to standardize scheme-related data.
 * Includes optional and required fields for scheme definition and its associated lines.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateSchemeRequestDTO implements BaseSchemeDTO {
    @NotBlank(message = "validation.scheme.scheme_name_required")
    private String schemeName;
    @NotNull(message = "validation.scheme.price")
    private Double price;
    private String units;
    private String fieldName;
    private String sourceLanguage;
    private String targetLanguage;
    private List<CreateSchemeLineRequestDTO> schemeLines;
}
