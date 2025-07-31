package com.frederic.clienttra.dto.bases;

/**
 * Base interface for Address DTOs.
 * Provides getter methods for common address fields.
 */
public interface BaseAddressDTO {
    String getStreet();
    String getStNumber();
    String getApt();
    String getCp();
    String getCity();
    String getState();
    String getCountry();
}
