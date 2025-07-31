package com.frederic.clienttra.dto.bases;

import java.time.LocalDate;
import java.util.List;

/**
 * Base interface for Order DTOs.
 * <p>
 * Defines the common structure for order-related data transfer objects,
 * including description, order date, unit price, units, total amount, billing status,
 * field name, source and target languages, and associated items.
 * <p>
 * The list of items uses a generic type extending BaseItemDTO to allow flexibility
 * in the specific item implementations.
 */
public interface BaseOrderDTO {
    String getDescrip();
    LocalDate getDateOrder();
    Double getPricePerUnit();
    String getUnits();
    Double getTotal();
    Boolean getBilled();
    String getFieldName();
    String getSourceLanguage();
    String getTargetLanguage();
    List<? extends BaseItemDTO> getItems();
}
