package com.frederic.clienttra.mappers;

import com.frederic.clienttra.dto.bases.BaseSchemeDTO;
import com.frederic.clienttra.dto.read.SchemeDTO;
import com.frederic.clienttra.dto.update.UpdateSchemeRequestDTO;
import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.entities.Scheme;
import com.frederic.clienttra.entities.SchemeLine;
import com.frederic.clienttra.exceptions.InvalidSchemeNameException;
import com.frederic.clienttra.exceptions.InvalidSchemePriceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper class for converting between Scheme entities and their DTO representations.
 */
@Component
@RequiredArgsConstructor
public class SchemeMapper {

    private final SchemeLineMapper schemeLineMapper;

    /**
     * Converts a list of Scheme entities to a list of SchemeDTOs.
     *
     * @param entities List of Scheme entities.
     * @return List of SchemeDTO objects.
     */
    public List<SchemeDTO> toDtos(List<Scheme> entities) {
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Converts a Scheme entity to a SchemeDTO.
     *
     * @param entity Scheme entity.
     * @return Corresponding SchemeDTO.
     */
    public SchemeDTO toDto(Scheme entity){
        return SchemeDTO.builder()
                .idScheme(entity.getIdScheme())
                .schemeName(entity.getSchemeName())
                .price(entity.getPrice())
                .units(entity.getUnits())
                .fieldName(entity.getFieldName())
                .sourceLanguage(entity.getSourceLanguage())
                .targetLanguage(entity.getTargetLanguage())
                .schemeLines(schemeLineMapper.toDtos(entity.getSchemeLines()))
                .build();
    }

    /**
     * Converts a BaseSchemeDTO to a Scheme entity.
     *
     * @param dto BaseSchemeDTO object.
     * @return Corresponding Scheme entity.
     */
    public Scheme toEntity(BaseSchemeDTO dto){
        return Scheme.builder()
                .schemeName(dto.getSchemeName())
                .price(dto.getPrice())
                .units(dto.getUnits())
                .fieldName(dto.getFieldName())
                .sourceLanguage(dto.getSourceLanguage())
                .targetLanguage(dto.getTargetLanguage())
                .schemeLines(schemeLineMapper.toEntities(dto.getSchemeLines()))
                .build();
    }

    /**
     * Converts a list of BaseSchemeDTOs (or subclasses) to a list of Scheme entities.
     *
     * @param dtos List of BaseSchemeDTO or its subclasses.
     * @return List of Scheme entities.
     */
    public List<Scheme> toEntities(List<? extends BaseSchemeDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Converts a BaseSchemeDTO to a Scheme entity linked to an owner company.
     * Sets the back-reference on each SchemeLine to this Scheme.
     *
     * @param dto BaseSchemeDTO object.
     * @param ownerCompany Company entity that owns this Scheme.
     * @return Scheme entity with ownerCompany and scheme lines properly linked.
     */
    public Scheme toEntity(BaseSchemeDTO dto, Company ownerCompany){
        Scheme scheme = Scheme.builder()
                .schemeName(dto.getSchemeName())
                .price(dto.getPrice())
                .units(dto.getUnits())
                .fieldName(dto.getFieldName())
                .sourceLanguage(dto.getSourceLanguage())
                .targetLanguage(dto.getTargetLanguage())
                .ownerCompany(ownerCompany)
                .schemeLines(schemeLineMapper.toEntities(dto.getSchemeLines()))
                .build();

        for(SchemeLine line : scheme.getSchemeLines()){
            line.setScheme(scheme);
        }

        return scheme;
    }

    /**
     * Updates an existing Scheme entity with data from an UpdateSchemeRequestDTO.
     * Performs validation for schemeName (non-blank) and price (positive).
     *
     * @param entity Existing Scheme entity to update.
     * @param dto DTO containing update data.
     * @throws InvalidSchemeNameException if schemeName is blank.
     * @throws InvalidSchemePriceException if price is zero or negative.
     */
    public void updateEntity(Scheme entity, UpdateSchemeRequestDTO dto){
        if(dto.getSchemeName() != null){
            if(dto.getSchemeName().isBlank()) {
                throw new InvalidSchemeNameException();
            }
            entity.setSchemeName(dto.getSchemeName());
        }
        if(dto.getPrice() != null){
            if(dto.getPrice() <= 0) {
                throw new InvalidSchemePriceException();
            }
            entity.setPrice(dto.getPrice());
        }
        if(dto.getUnits() != null){
            entity.setUnits(dto.getUnits());
        }
        if(dto.getFieldName() != null){
            entity.setFieldName(dto.getFieldName());
        }
        if(dto.getSourceLanguage() != null){
            entity.setSourceLanguage(dto.getSourceLanguage());
        }
        if(dto.getTargetLanguage() != null){
            entity.setTargetLanguage(dto.getTargetLanguage());
        }
        /* Uncomment to update scheme lines as well, if needed
        if(dto.getSchemeLines() != null && !dto.getSchemeLines().isEmpty()){
            schemeLineMapper.updateEntities(entity,dto.getSchemeLines());
        }
        */
    }
}
