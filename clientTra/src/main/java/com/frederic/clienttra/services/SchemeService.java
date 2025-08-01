package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.create.CreateSchemeRequestDTO;
import com.frederic.clienttra.dto.read.SchemeDTO;
import com.frederic.clienttra.dto.update.UpdateSchemeLineRequestDTO;
import com.frederic.clienttra.dto.update.UpdateSchemeRequestDTO;
import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.entities.Scheme;
import com.frederic.clienttra.entities.SchemeLine;
import com.frederic.clienttra.exceptions.SchemeNotFoundException;
import com.frederic.clienttra.mappers.SchemeLineMapper;
import com.frederic.clienttra.mappers.SchemeMapper;
import com.frederic.clienttra.repositories.SchemeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for managing Schemes and their SchemeLines.
 * Allows retrieval, creation, update and deletion of schemes belonging to companies.
 */
@Service
@RequiredArgsConstructor
public class SchemeService {

    private final CompanyService companyService;
    private final SchemeRepository schemeRepository;
    private final SchemeMapper schemeMapper;
    private final SchemeLineMapper schemeLineMapper;

    /**
     * Retrieves all schemes belonging to a company owned by the current user's company.
     *
     * @param idCompany the ID of the company whose schemes are requested.
     * @return a list of SchemeDTO objects.
     */
    @Transactional(readOnly = true)
    public List<SchemeDTO> getAllSchemes(Integer idCompany){
        Company owner = companyService.getCurrentCompanyOrThrow();

        List<Scheme> entities = schemeRepository.findByOwnerCompanyAndCompany_idCompany(owner, idCompany);

        return schemeMapper.toDtos(entities);
    }

    /**
     * Retrieves a specific scheme by its ID and company ID.
     * Throws SchemeNotFoundException if not found or if scheme doesn't belong to the company.
     *
     * @param idCompany the ID of the company.
     * @param idScheme the ID of the scheme.
     * @return the SchemeDTO representing the scheme.
     * @throws SchemeNotFoundException if the scheme is not found or does not belong to the company.
     */
    @Transactional(readOnly = true)
    public SchemeDTO getScheme(Integer idCompany, Integer idScheme){
        Company owner = companyService.getCurrentCompanyOrThrow();

        Scheme entity = schemeRepository.findByOwnerCompanyAndIdScheme(owner, idScheme)
                .orElseThrow(SchemeNotFoundException::new);
        if (!Objects.equals(entity.getCompany().getIdCompany(), idCompany)) {
            throw new SchemeNotFoundException();
        }

        return schemeMapper.toDto(entity);
    }

    /**
     * Creates a new scheme for a given company with optional scheme lines.
     *
     * @param idCompany the ID of the company.
     * @param dto the DTO containing the scheme data including optional scheme lines.
     */
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

    /**
     * Updates an existing scheme and its scheme lines.
     * Only updates scheme lines present in the DTO, adds new ones, and removes those not included.
     * Throws SchemeNotFoundException if the scheme doesn't belong to the company or does not exist.
     *
     * @param idCompany the ID of the company.
     * @param idScheme the ID of the scheme.
     * @param dto the DTO containing updated scheme data and scheme lines.
     * @return the updated SchemeDTO.
     * @throws SchemeNotFoundException if the scheme is not found or does not belong to the company.
     */
    @Transactional
    public SchemeDTO updateScheme(Integer idCompany, Integer idScheme, UpdateSchemeRequestDTO dto) {
        Company owner = companyService.getCurrentCompanyOrThrow();

        Scheme entity = schemeRepository.findByOwnerCompanyAndIdScheme(owner, idScheme)
                .orElseThrow(SchemeNotFoundException::new);

        if (!Objects.equals(entity.getCompany().getIdCompany(), idCompany)) {
            throw new SchemeNotFoundException();
        }

        // Update simple fields
        schemeMapper.updateEntity(entity, dto);

        // Update scheme lines
        List<UpdateSchemeLineRequestDTO> updatedItemsDTO = dto.getSchemeLines() != null ? dto.getSchemeLines() : Collections.emptyList();

        Map<Integer, SchemeLine> existingLinesMap = entity.getSchemeLines().stream()
                .filter(i -> i.getIdSchemeLine() != null)
                .collect(Collectors.toMap(SchemeLine::getIdSchemeLine, i -> i));

        List<SchemeLine> updatedLines = new ArrayList<>();

        for (UpdateSchemeLineRequestDTO lineDTO : updatedItemsDTO) {
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

    /**
     * Deletes a scheme by its ID for a given company.
     * Throws SchemeNotFoundException if the scheme doesn't belong to the company or does not exist.
     *
     * @param idCompany the ID of the company.
     * @param idScheme the ID of the scheme to delete.
     * @throws SchemeNotFoundException if the scheme is not found or does not belong to the company.
     */
    @Transactional
    public void deleteScheme(Integer idCompany, Integer idScheme){
        Company owner = companyService.getCurrentCompanyOrThrow();
        Scheme entity = schemeRepository.findByOwnerCompanyAndIdScheme(owner, idScheme)
                .orElseThrow(SchemeNotFoundException::new);
        if (!Objects.equals(entity.getCompany().getIdCompany(), idCompany)) {
            throw new SchemeNotFoundException();
        }
        schemeRepository.delete(entity);
    }
}
