package com.frederic.clienttra.mappers;

import com.frederic.clienttra.dto.read.BaseCompanyMinimalDTO;
import com.frederic.clienttra.entities.Company;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting Company entities to minimal DTOs.
 * <p>
 * This class provides a lightweight mapping to avoid circular dependencies
 * between CompanyMapper, DocumentMapper, and OrderMapper.
 * </p>
 */
@Component
public class MinimalCompanyMapper {

    /**
     * Converts a Company entity to a BaseCompanyMinimalDTO.
     *
     * @param entity the Company entity to convert
     * @return the minimal DTO containing company ID, VAT number, and commercial name
     */
    public BaseCompanyMinimalDTO toBaseCompanyMinimalDTO(Company entity){
        return BaseCompanyMinimalDTO.builder()
                .idCompany(entity.getIdCompany())
                .vatNumber(entity.getVatNumber())
                .comName(entity.getComName())
                .build();
    }

}
