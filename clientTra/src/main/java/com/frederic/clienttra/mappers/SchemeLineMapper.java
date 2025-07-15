package com.frederic.clienttra.mappers;

import com.frederic.clienttra.dto.bases.BaseSchemeLineDTO;
import com.frederic.clienttra.dto.read.SchemeLineDTO;
import com.frederic.clienttra.dto.update.UpdateSchemeLineRequestDTO;
import com.frederic.clienttra.entities.Scheme;
import com.frederic.clienttra.entities.SchemeLine;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class SchemeLineMapper {

    public List<SchemeLineDTO> toDtos(List<SchemeLine> entities) {
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public SchemeLineDTO toDto(SchemeLine entity) {
        return SchemeLineDTO.builder()
                .descrip(entity.getDescrip())
                .discount(entity.getDiscount())
                .build();
    }

    public SchemeLine toEntity(BaseSchemeLineDTO dto) {
        return SchemeLine.builder()
                .descrip(dto.getDescrip())
                .discount(dto.getDiscount())
                .build();
    }

    public List<SchemeLine> toEntities(List<? extends BaseSchemeLineDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void updateEntities(Scheme entity, List<UpdateSchemeLineRequestDTO> dtos){
        Map<Integer, SchemeLine> existingLinesMap = entity.getSchemeLines().stream()
            .filter(line -> line.getIdSchemeLine() != null)
            .collect(Collectors.toMap(SchemeLine::getIdSchemeLine, Function.identity()));

        for (UpdateSchemeLineRequestDTO dto : dtos) {
            Integer id = dto.getIdSchemeLine();
            if (id != null && existingLinesMap.containsKey(id)) {
                SchemeLine line = existingLinesMap.get(id);
                if (dto.getDescrip() != null) {
                    line.setDescrip(dto.getDescrip());
                }
                if (dto.getDiscount() != null) {
                    line.setDiscount(dto.getDiscount());
                }
            }
        }
    }
}

