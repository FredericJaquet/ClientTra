package com.frederic.clienttra.dto.create;

import com.frederic.clienttra.dto.bases.BasePhoneDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePhoneRequestDTO implements BasePhoneDTO {
    @NotBlank(message = "validation.phone.number_required")
    private String phoneNumber;
    private String kind;
}