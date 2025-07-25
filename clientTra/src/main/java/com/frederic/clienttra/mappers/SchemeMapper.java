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

@Component
@RequiredArgsConstructor
public class SchemeMapper {
    private final SchemeLineMapper schemeLineMapper;

    public List<SchemeDTO> toDtos(List<Scheme> entities) {
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public SchemeDTO toDto(Scheme entity){
        return SchemeDTO.builder()
                .idScheme((entity.getIdScheme()))
                .schemeName(entity.getSchemeName())
                .price(entity.getPrice())
                .units(entity.getUnits())
                .fieldName(entity.getFieldName())
                .sourceLanguage(entity.getSourceLanguage())
                .targetLanguage(entity.getTargetLanguage())
                .schemeLines(schemeLineMapper.toDtos(entity.getSchemeLines()))
                .build();
    }

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

    public List<Scheme> toEntities(List<? extends BaseSchemeDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public Scheme toEntity(BaseSchemeDTO dto, Company ownercompany){
        Scheme scheme = Scheme.builder()
                .schemeName(dto.getSchemeName())
                .price(dto.getPrice())
                .units(dto.getUnits())
                .fieldName(dto.getFieldName())
                .sourceLanguage(dto.getSourceLanguage())
                .targetLanguage(dto.getTargetLanguage())
                .ownerCompany(ownercompany)
                .schemeLines(schemeLineMapper.toEntities(dto.getSchemeLines()))
                .build();

        for(SchemeLine line:scheme.getSchemeLines()){
            line.setScheme(scheme);
        }

        return scheme;
    }

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
        /*if(dto.getSchemeLines() != null && !dto.getSchemeLines().isEmpty()){
            schemeLineMapper.updateEntities(entity,dto.getSchemeLines());
        }*/
    }

}
