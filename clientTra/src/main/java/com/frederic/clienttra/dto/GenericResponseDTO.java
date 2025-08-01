package com.frederic.clienttra.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Generic response DTO containing a message.
 * Used to send simple textual responses from the backend.
 */
@Data
@AllArgsConstructor
public class GenericResponseDTO {
    private String message;
}
