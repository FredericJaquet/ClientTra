package com.frederic.clienttra.dto.bases;

/**
 * Base interface for Phone DTOs.
 * <p>
 * Defines the structure for phone-related data transfer objects,
 * including the phone number and its type/kind.
 */
public interface BasePhoneDTO {
    String getPhoneNumber();
    String getKind();
}
