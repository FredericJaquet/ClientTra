package com.frederic.clienttra.dto.bases;

import java.util.List;

/**
 * Base interface for Scheme DTOs.
 * <p>
 * Defines the structure for scheme-related data transfer objects,
 * including scheme details and its associated scheme lines.
 */
public interface BaseSchemeDTO {
    String getSchemeName();
    Double getPrice();
    String getUnits();
    String getFieldName();
    String getSourceLanguage();
    String getTargetLanguage();
    List<? extends BaseSchemeLineDTO> getSchemeLines();
}

