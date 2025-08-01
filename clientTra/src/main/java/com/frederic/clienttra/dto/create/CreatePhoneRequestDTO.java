package com.frederic.clienttra.dto.create;

import com.frederic.clienttra.dto.bases.BasePhoneDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO used to create a phone entry for a company or contact person.
 * Implements the BasePhoneDTO interface and includes basic validation.
 * Typically used when submitting new or additional phone information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePhoneRequestDTO implements BasePhoneDTO {
    @NotBlank(message = "{validation.phone.number_required}")
    private String phoneNumber;
    private String kind;
}