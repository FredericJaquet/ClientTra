package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.create.CreateChangeRateRequestDTO;
import com.frederic.clienttra.dto.read.ChangeRateDTO;
import com.frederic.clienttra.dto.update.UpdateChangeRateRequestDTO;
import com.frederic.clienttra.entities.ChangeRate;
import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.exceptions.ChangeRateNotFoundException;
import com.frederic.clienttra.mappers.ChangeRateMapper;
import com.frederic.clienttra.repositories.ChangeRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChangeRateService {

    private final CompanyService companyService;
    private final ChangeRateRepository changeRateRepository;
    private final ChangeRateMapper changeRateMapper;

    @Transactional(readOnly = true)
    public List<ChangeRateDTO> getAllChangeRates() {
        Company owner = companyService.getCurrentCompanyOrThrow();
        return changeRateMapper.toDtos(changeRateRepository.findByOwnerCompany(owner));
    }

    @Transactional(readOnly = true)
    public ChangeRateDTO getChangeRateById(Integer idChangeRate) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        ChangeRate entity = changeRateRepository.findByOwnerCompanyAndIdChangeRate(owner, idChangeRate)
                .orElseThrow(ChangeRateNotFoundException::new);
        return changeRateMapper.toDto(entity);
    }

    @Transactional
    public ChangeRateDTO createChangeRate(CreateChangeRateRequestDTO dto) {
        Company owner = companyService.getCurrentCompanyOrThrow();

        ChangeRate entity = changeRateMapper.toEntity(dto);
        entity.setOwnerCompany(owner);
        changeRateRepository.save(entity);
        return changeRateMapper.toDto(entity);
    }

    @Transactional
    public ChangeRateDTO updateChangeRate(Integer idChangeRate, UpdateChangeRateRequestDTO dto) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        ChangeRate entity = changeRateRepository.findByOwnerCompanyAndIdChangeRate(owner, idChangeRate)
                .orElseThrow(ChangeRateNotFoundException::new);

        changeRateMapper.updateEntity(entity, dto);
        changeRateRepository.save(entity);
        return changeRateMapper.toDto(entity);
    }

    @Transactional
    public void deleteChangeRate(Integer idChangeRate) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        ChangeRate entity = changeRateRepository.findByOwnerCompanyAndIdChangeRate(owner, idChangeRate)
                .orElseThrow(ChangeRateNotFoundException::new);
        changeRateRepository.delete(entity);
    }
}
