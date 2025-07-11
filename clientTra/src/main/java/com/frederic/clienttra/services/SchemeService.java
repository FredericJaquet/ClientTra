package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.create.CreateSchemeRequestDTO;
import com.frederic.clienttra.dto.read.SchemeDTO;
import com.frederic.clienttra.dto.update.UpdateItemRequestDTO;
import com.frederic.clienttra.dto.update.UpdateSchemeLineRequestDTO;
import com.frederic.clienttra.dto.update.UpdateSchemeRequestDTO;
import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.entities.Item;
import com.frederic.clienttra.entities.Scheme;
import com.frederic.clienttra.entities.SchemeLine;
import com.frederic.clienttra.exceptions.InvalidSchemeNameException;
import com.frederic.clienttra.exceptions.InvalidSchemePriceException;
import com.frederic.clienttra.exceptions.SchemeNotFoundException;
import com.frederic.clienttra.mappers.SchemeLineMapper;
import com.frederic.clienttra.mappers.SchemeMapper;
import com.frederic.clienttra.repositories.SchemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchemeService {

    private final CompanyService companyService;
    private final SchemeRepository schemeRepository;
    private final SchemeMapper schemeMapper;
    private final SchemeLineMapper schemeLineMapper;

    @Transactional(readOnly = true)
    public List<SchemeDTO> getAllSchemes(Integer idCompany){
        Company owner = companyService.getCurrentCompanyOrThrow();

        List<Scheme> entities=schemeRepository.findByOwnerCompanyAndCompany_idCompany(owner,idCompany);

        return schemeMapper.toDtos(entities);
    }

    @Transactional(readOnly = true)
    public SchemeDTO getScheme(Integer idCompany, Integer idScheme){
        Company owner = companyService.getCurrentCompanyOrThrow();

        Scheme entity=schemeRepository.findByOwnerCompanyAndIdScheme(owner,idScheme)
                .orElseThrow(SchemeNotFoundException::new);
        if(!Objects.equals(entity.getCompany().getIdCompany(), idCompany)){
            throw new SchemeNotFoundException();
        }

        return schemeMapper.toDto(entity);
    }

    @Transactional
    public void createScheme(Integer idCompany, CreateSchemeRequestDTO dto) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        Company company = companyService.getCompanyById(idCompany);
        Scheme entity = schemeMapper.toEntity(dto);
        entity.setOwnerCompany(owner);
        entity.setCompany(company);

        if (dto.getSchemeLines() != null && !dto.getSchemeLines().isEmpty()) {
            List<SchemeLine> lines = dto.getSchemeLines().stream()
                    .map(lineDto -> {
                        SchemeLine lineEntity = schemeLineMapper.toEntity(lineDto);
                        lineEntity.setScheme(entity);
                        return lineEntity;
                    })
                    .toList();

            entity.setSchemeLines(lines);
        }

        schemeRepository.save(entity);
    }

    @Transactional
    public SchemeDTO updateScheme(Integer idCompany, Integer idScheme, UpdateSchemeRequestDTO dto) {
        Company owner = companyService.getCurrentCompanyOrThrow();

        Scheme entity = schemeRepository.findByOwnerCompanyAndIdScheme(owner, idScheme)
                .orElseThrow(SchemeNotFoundException::new);

        if (!Objects.equals(entity.getCompany().getIdCompany(), idCompany)) {
            throw new SchemeNotFoundException();
        }

        // Actualizar campos simples
        schemeMapper.updateEntity(entity,dto);

        // Actualizar líneas
        List<UpdateSchemeLineRequestDTO> updatedItemsDTO = dto.getSchemeLines() != null ? dto.getSchemeLines() : Collections.emptyList();

        Map<Integer, SchemeLine> existingLinesMap = entity.getSchemeLines().stream()
                .filter(i -> i.getIdSchemeLine() != null)
                .collect(Collectors.toMap(SchemeLine::getIdSchemeLine, i -> i));

        List<SchemeLine> updatedLines = new ArrayList<>();

        for(UpdateSchemeLineRequestDTO lineDTO : updatedItemsDTO) {
            if (lineDTO.getIdSchemeLine() != null && existingLinesMap.containsKey(lineDTO.getIdSchemeLine())) {
                SchemeLine line = existingLinesMap.get(lineDTO.getIdSchemeLine());
                if (lineDTO.getDescrip() != null) {
                    line.setDescrip(lineDTO.getDescrip());
                }
                if (lineDTO.getDiscount() != null) {
                    line.setDiscount(lineDTO.getDiscount());
                }
                updatedLines.add(line);
                existingLinesMap.remove(lineDTO.getIdSchemeLine());
            } else {
                SchemeLine newLine = schemeLineMapper.toEntity(lineDTO);
                newLine.setScheme(entity);
                updatedLines.add(newLine);
            }
        }
        entity.getSchemeLines().clear();
        entity.getSchemeLines().addAll(updatedLines);
        schemeRepository.save(entity);

        return schemeMapper.toDto(entity);
    }


    @Transactional
    public void deleteScheme(Integer idCompany, Integer idScheme){
        Company owner = companyService.getCurrentCompanyOrThrow();
        Scheme entity = schemeRepository.findByOwnerCompanyAndIdScheme(owner, idScheme)
                .orElseThrow(SchemeNotFoundException::new);
        if(!Objects.equals(entity.getCompany().getIdCompany(), idCompany)){
            throw new SchemeNotFoundException();
        }
        schemeRepository.delete(entity);
    }
}
