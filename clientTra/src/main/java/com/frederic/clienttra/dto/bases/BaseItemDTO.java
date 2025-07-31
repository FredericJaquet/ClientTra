package com.frederic.clienttra.dto.bases;

/**
 * Base interface for Item DTOs.
 * <p>
 * Defines the common contract for item-related data transfer objects,
 * including description, quantity, discount, and total amount.
 * <p>
 * Implementations of this interface are used to transfer item data within the application layers.
 */
public interface BaseItemDTO {
    String getDescrip();
    Double getQty();
    Double getDiscount();
    Double getTotal();
}

