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

/**
 * Mapper class for converting between SchemeLine entities and their DTO representations.
 */
@Component
public class SchemeLineMapper {

    /**
     * Converts a list of SchemeLine entities to a list of SchemeLineDTOs.
     *
     * @param entities List of SchemeLine entities.
     * @return List of SchemeLineDTO objects.
     */
    public List<SchemeLineDTO> toDtos(List<SchemeLine> entities) {
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Converts a SchemeLine entity to a SchemeLineDTO.
     *
     * @param entity SchemeLine entity.
     * @return Corresponding SchemeLineDTO.
     */
    public SchemeLineDTO toDto(SchemeLine entity) {
        return SchemeLineDTO.builder()
                .descrip(entity.getDescrip())
                .discount(entity.getDiscount())
                .build();
    }

    /**
     * Converts a BaseSchemeLineDTO to a SchemeLine entity.
     *
     * @param dto BaseSchemeLineDTO object.
     * @return Corresponding SchemeLine entity.
     */
    public SchemeLine toEntity(BaseSchemeLineDTO dto) {
        return SchemeLine.builder()
                .descrip(dto.getDescrip())
                .discount(dto.getDiscount())
                .build();
    }

    /**
     * Converts a list of BaseSchemeLineDTOs (or subclasses) to a list of SchemeLine entities.
     *
     * @param dtos List of BaseSchemeLineDTO or its subclasses.
     * @return List of SchemeLine entities.
     */
    public List<SchemeLine> toEntities(List<? extends BaseSchemeLineDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Updates existing SchemeLine entities in a Scheme entity from a list of UpdateSchemeLineRequestDTOs.
     * Only lines with matching IDs will be updated.
     *
     * @param entity Scheme entity containing existing SchemeLines.
     * @param dtos List of DTOs with updated data.
     */
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
