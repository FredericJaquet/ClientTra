package com.frederic.clienttra.dto.create;

import com.frederic.clienttra.dto.bases.BaseOrderDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO used to create a new order.
 * Implements the BaseOrderDTO interface and includes validation constraints
 * for required fields such as description, date, price, and associated items.
 * This class is used for input data when a new order is submitted by a client.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderRequestDTO implements BaseOrderDTO {
    @NotBlank(message = "validation.order.description_required")
    private String descrip;
    @NotNull(message = "validation.order.date_required")
    private LocalDate dateOrder;
    @NotNull(message = "validation.order.price_required")
    private Double pricePerUnit;
    private String units;
    private Double total = 0.0;
    private Boolean billed;
    private String fieldName;
    private String sourceLanguage;
    private String targetLanguage;
    @NotEmpty(message = "validation.order.item_required")
    private List<CreateItemRequestDTO> items;
}
