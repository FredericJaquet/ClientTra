package com.frederic.clienttra.dto.read;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
