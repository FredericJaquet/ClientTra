package com.frederic.clienttra.dto.bases;

/**
 * Base interface for Contact Person DTOs.
 * Defines getter methods for basic contact person details.
 */
public interface BaseContactPersonDTO {
    String getFirstname();
    String getMiddlename();
    String getLastname();
    String getRole();
    String getEmail();
}
