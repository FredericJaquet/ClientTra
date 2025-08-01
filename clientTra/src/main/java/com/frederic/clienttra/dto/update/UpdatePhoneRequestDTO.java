package com.frederic.clienttra.dto.update;

import com.frederic.clienttra.dto.bases.BasePhoneDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating a phone number.
 * Implements the BasePhoneDTO interface.
 * Note: The phoneNumber field should not be blank (validation handled elsewhere).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePhoneRequestDTO implements BasePhoneDTO {
    private String phoneNumber;
    private String kind;
}