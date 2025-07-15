package com.frederic.clienttra.mappers;

import com.frederic.clienttra.dto.read.BaseCompanyMinimalDTO;
import com.frederic.clienttra.entities.Company;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class MinimalCompanyMapper {//Necesario, se usa en OrderMapper para no tener un bucle entre CompanyMapper -> DocumentMapper -> OrderMapper

    public BaseCompanyMinimalDTO toBaseCompanyMinimalDTO(Company entity){
        return BaseCompanyMinimalDTO.builder()
                .idCompany(entity.getIdCompany())
                .vatNumber(entity.getVatNumber())
                .comName(entity.getComName())
                .build();
    }

}
