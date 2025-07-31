package com.frederic.clienttra.dto.bases;

/**
 * Base interface for Scheme Line DTOs.
 * <p>
 * Defines the structure for individual lines within a scheme,
 * including description and discount information.
 */
public interface BaseSchemeLineDTO {
    String getDescrip();
    Double getDiscount();
}

