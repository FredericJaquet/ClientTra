package com.frederic.clienttra.dto.read;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for representing a provider in list views.
 * Contains basic identification and contact information along with status.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProviderForListDTO {
    private Integer idProvider;
    private String comName;
    private String vatNumber;
    private String email;
    private String web;
    private Boolean enabled;

}
