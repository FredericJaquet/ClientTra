package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.create.CreateProviderRequestDTO;
import com.frederic.clienttra.dto.read.BaseCompanyMinimalDTO;
import com.frederic.clienttra.dto.read.ProviderDetailsDTO;
import com.frederic.clienttra.dto.read.ProviderForListDTO;
import com.frederic.clienttra.dto.update.UpdateProviderRequestDTO;
import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.entities.Provider;
import com.frederic.clienttra.exceptions.ProviderNotFoundException;
import com.frederic.clienttra.mappers.CompanyMapper;
import com.frederic.clienttra.mappers.ProviderMapper;
import com.frederic.clienttra.projections.ProviderListProjection;
import com.frederic.clienttra.projections.ProviderMinimalProjection;
import com.frederic.clienttra.repositories.CompanyRepository;
import com.frederic.clienttra.repositories.ProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProviderServiceImpl implements  ProviderService{

    private final ProviderRepository providerRepository;
    private final CompanyRepository companyRepository;
    private final ProviderMapper providerMapper;
    private final CompanyMapper companyMapper;
    private final CompanyService companyService;

    @Transactional(readOnly = true)
    @Override
    public List<ProviderForListDTO> getAllProviders() {
        Company owner = companyService.getCurrentCompanyOrThrow();
        List<ProviderListProjection> entities = new ArrayList<>(providerRepository.findListByOwnerCompany(owner));
        List<ProviderForListDTO> dtos=providerMapper.toProviderForListDTOS(entities);
        dtos.sort(Comparator.comparing(ProviderForListDTO::getComName, Comparator.nullsLast((String.CASE_INSENSITIVE_ORDER))));

        return dtos;
    }

    @Transactional(readOnly = true)
    @Override
    public ProviderDetailsDTO getProviderById(int id) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        Provider entity = providerRepository.findByOwnerCompanyAndIdProvider(owner,id)
                .orElseThrow(ProviderNotFoundException::new);

        return providerMapper.toProviderDetailsDTO(entity);
    }

    @Transactional
    @Override
    public int createProvider(CreateProviderRequestDTO dto) {
        Company owner = companyService.getCurrentCompanyOrThrow();

        Company companyEntity = companyMapper.toEntity(dto);
        companyEntity.setOwnerCompany(owner);
        Company savedCompany = companyRepository.save(companyEntity);

        Provider entity = providerMapper.toEntity(dto);
        entity.setCompany(savedCompany);
        entity.setOwnerCompany(owner);
        entity.setEnabled(true);

        Provider providerSaved = providerRepository.save(entity);

        return providerSaved.getIdProvider();
    }

    @Transactional
    @Override
    public void updateProvider(int id, UpdateProviderRequestDTO dto) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        Provider entity = providerRepository.findByOwnerCompanyAndIdProvider(owner,id)
                .orElseThrow(ProviderNotFoundException::new);
        Company company = entity.getCompany();
        companyMapper.updateEntity(company,dto);
        providerMapper.updateEntity(entity,dto);

        companyRepository.save(company);
        providerRepository.save(entity);
    }
    @Transactional
    @Override
    public void disableProvider(int id) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        Provider entity = providerRepository.findByOwnerCompanyAndIdProvider(owner,id)
                .orElseThrow(ProviderNotFoundException::new);
        entity.setEnabled(false);
        providerRepository.save(entity);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProviderForListDTO> searchByNameOrVat(String input) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        String query = "%" + input + "%";

        List<ProviderListProjection> entities = new ArrayList<>(providerRepository.findListByComNameOrLegalNameOrVatNumber(owner,query));
        List<ProviderForListDTO> dtos=providerMapper.toProviderForListDTOS(entities);
        dtos.sort(Comparator.comparing(ProviderForListDTO::getComName, Comparator.nullsLast((String.CASE_INSENSITIVE_ORDER))));

        return dtos;
    }

    @Transactional(readOnly = true)
    @Override
    public List<BaseCompanyMinimalDTO> getMinimalProviderList() {
        Company owner = companyService.getCurrentCompanyOrThrow();
        List<ProviderMinimalProjection> entities = providerRepository.findMinimalListByOwnerCompany(owner);
        return providerMapper.toMinimalDTOs(entities);
    }
}
