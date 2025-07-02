package com.frederic.clienttra.mappers;

import com.frederic.clienttra.dto.read.SchemeLineDTO;
import com.frederic.clienttra.entities.SchemeLine;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SchemeLineMapper {

    public List<SchemeLineDTO> toDTOs(List<SchemeLine> entities) {
        return entities.stream()
                .map(this::toDTO)
                .toList();
    }

    public SchemeLineDTO toDTO(SchemeLine entity) {
        return SchemeLineDTO.builder()
                .descrip(entity.getDescrip())
                .discount(entity.getDiscount())
                .build();
    }

    public SchemeLine toEntity(SchemeLineDTO dto) {
        return SchemeLine.builder()
                .descrip(dto.getDescrip())
                .discount(dto.getDiscount())
                .build();
    }

    public List<SchemeLine> toEntities(List<SchemeLineDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .toList();
    }
}

