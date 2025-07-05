package com.frederic.clienttra.dto.read;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseCompanyMinimalDTO {//Sirve para rellenar los 2 campos de información que aparecen en un pedido al seleccionar el cliente
    private Integer idCompany;
    private String comName;
    private String vatNumber;

}
