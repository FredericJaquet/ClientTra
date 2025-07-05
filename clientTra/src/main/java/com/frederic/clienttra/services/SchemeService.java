package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.create.CreateSchemeRequestDTO;
import com.frederic.clienttra.dto.read.SchemeDTO;
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

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SchemeService {

    private final CompanyService companyService;
    private final SchemeRepository schemeRepository;
    private final SchemeMapper schemeMapper;
    private final SchemeLineMapper schemeLineMapper;


    public List<SchemeDTO> getAllSchemes(Integer idCompany){
        Company owner = companyService.getCurrentCompanyOrThrow();

        List<Scheme> entities=schemeRepository.findByOwnerCompanyAndCompany_idCompany(owner,idCompany);

        return schemeMapper.toDTOs(entities);
    }

    public SchemeDTO getScheme(Integer idCompany, Integer idScheme){
        Company owner = companyService.getCurrentCompanyOrThrow();

        Scheme entity=schemeRepository.findByOwnerCompanyAndIdScheme(owner,idScheme)
                .orElseThrow(SchemeNotFoundException::new);
        if(!Objects.equals(entity.getCompany().getIdCompany(), idCompany)){
            throw new SchemeNotFoundException();
        }

        return schemeMapper.toDTO(entity);
    }

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


    public SchemeDTO updateScheme(Integer idCompany, Integer idScheme, UpdateSchemeRequestDTO dto) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        Scheme entity = schemeRepository.findByOwnerCompanyAndIdScheme(owner, idScheme)
                .orElseThrow(SchemeNotFoundException::new);

        if(!Objects.equals(entity.getCompany().getIdCompany(), idCompany)){
            throw new SchemeNotFoundException();
        }

        schemeMapper.updateEntity(entity, dto);

        schemeRepository.save(entity);

        return schemeMapper.toDTO(entity);
    }

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
