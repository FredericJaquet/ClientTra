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

/**
 * Implementation of the ProviderService interface.
 * Provides methods to manage providers including creation,
 * retrieval, updating, searching, and disabling.
 */
@Service
@RequiredArgsConstructor
public class ProviderServiceImpl implements ProviderService {

    private final ProviderRepository providerRepository;
    private final CompanyRepository companyRepository;
    private final ProviderMapper providerMapper;
    private final CompanyMapper companyMapper;
    private final CompanyService companyService;

    /**
     * Retrieves all enabled providers for the current owner company,
     * sorted by commercial name (case-insensitive).
     *
     * @return List of all enabled providers.
     */
    @Transactional(readOnly = true)
    @Override
    public List<ProviderForListDTO> getAllProviders() {
        Company owner = companyService.getCurrentCompanyOrThrow();

        List<ProviderListProjection> entities = new ArrayList<>(providerRepository.findListByOwnerCompany(owner));
        List<ProviderForListDTO> dtos = providerMapper.toProviderForListDTOS(entities);

        // Sort providers by commercial name, nulls last, case-insensitive
        dtos.sort(Comparator.comparing(ProviderForListDTO::getComName, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)));

        return dtos;
    }

    /**
     * Retrieves providers filtered by enabled status for the current owner,
     * sorted by commercial name (case-insensitive).
     *
     * @param enabled true to retrieve enabled providers, false for disabled.
     * @return List of filtered providers.
     */
    public List<ProviderForListDTO> getAllProvidersEnabled(boolean enabled) {
        Company owner = companyService.getCurrentCompanyOrThrow();

        List<ProviderListProjection> entities = new ArrayList<>(providerRepository.findListByOwnerCompany(owner, enabled));
        List<ProviderForListDTO> dtos = providerMapper.toProviderForListDTOS(entities);

        dtos.sort(Comparator.comparing(ProviderForListDTO::getComName, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)));

        return dtos;
    }

    /**
     * Retrieves detailed information for a provider by its ID,
     * ensuring it belongs to the current owner company.
     *
     * @param id provider ID.
     * @return Provider details DTO.
     * @throws ProviderNotFoundException if no matching provider found.
     */
    @Transactional(readOnly = true)
    @Override
    public ProviderDetailsDTO getProviderById(int id) {
        Company owner = companyService.getCurrentCompanyOrThrow();

        Provider entity = providerRepository.findByOwnerCompanyAndIdProvider(owner, id)
                .orElseThrow(ProviderNotFoundException::new);

        return providerMapper.toProviderDetailsDTO(entity);
    }

    /**
     * Searches providers by partial match on commercial name,
     * legal name or VAT number, within the current owner company.
     *
     * @param input search query string.
     * @return List of matching providers.
     */
    @Transactional(readOnly = true)
    @Override
    public List<ProviderForListDTO> searchByNameOrVat(String input) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        String query = "%" + input + "%"; // wildcard search

        List<ProviderListProjection> entities = new ArrayList<>(providerRepository.findListByComNameOrLegalNameOrVatNumber(owner, query));
        List<ProviderForListDTO> dtos = providerMapper.toProviderForListDTOS(entities);

        dtos.sort(Comparator.comparing(ProviderForListDTO::getComName, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)));

        return dtos;
    }

    /**
     * Retrieves a minimal list of providers with only essential company information,
     * for the current owner company.
     *
     * @return List of minimal provider DTOs.
     */
    @Transactional(readOnly = true)
    @Override
    public List<BaseCompanyMinimalDTO> getMinimalProviderList() {
        Company owner = companyService.getCurrentCompanyOrThrow();

        List<ProviderMinimalProjection> entities = providerRepository.findMinimalListByOwnerCompany(owner);
        return providerMapper.toMinimalDTOs(entities);
    }

    /**
     * Creates a new provider along with its company entity,
     * associating both with the current owner company.
     *
     * @param dto CreateProviderRequestDTO with provider and company data.
     * @return The ID of the newly created provider.
     */
    @Transactional
    @Override
    public ProviderForListDTO createProvider(CreateProviderRequestDTO dto) {
        Company owner = companyService.getCurrentCompanyOrThrow();

        Company companyEntity = companyMapper.toEntity(dto);
        companyEntity.setOwnerCompany(owner);
        Company savedCompany = companyRepository.save(companyEntity);

        if(dto.getDefaultVat()>1){
            dto.setDefaultVat(dto.getDefaultVat()/100);
        }

        if(dto.getDefaultWithholding()>1){
            dto.setDefaultWithholding(dto.getDefaultWithholding()/100);
        }

        Provider entity = providerMapper.toEntity(dto);
        entity.setCompany(savedCompany);
        entity.setOwnerCompany(owner);

        Provider providerSaved = providerRepository.save(entity);

        return providerMapper.toProviderForListDTO(providerSaved);
    }

    /**
     * Updates an existing provider and its associated company information.
     *
     * @param id  The provider ID to update.
     * @param dto UpdateProviderRequestDTO containing updated data.
     * @throws ProviderNotFoundException if the provider does not exist.
     */
    @Transactional
    @Override
    public void updateProvider(int id, UpdateProviderRequestDTO dto) {
        Company owner = companyService.getCurrentCompanyOrThrow();

        Provider entity = providerRepository.findByOwnerCompanyAndIdProvider(owner, id)
                .orElseThrow(ProviderNotFoundException::new);

        Company company = entity.getCompany();

        if(dto.getDefaultVat()>1){
            dto.setDefaultVat(dto.getDefaultVat()/100);
        }

        if(dto.getDefaultWithholding()>1){
            dto.setDefaultWithholding(dto.getDefaultWithholding()/100);
        }

        // Update company and provider entities from DTO
        companyMapper.updateEntity(company, dto);
        providerMapper.updateEntity(entity, dto);

        companyRepository.save(company);
        providerRepository.save(entity);
    }

    /**
     * Disables a provider by setting its enabled flag to false.
     *
     * @param id Provider ID to disable.
     * @throws ProviderNotFoundException if provider not found.
     */
    @Transactional
    @Override
    public void disableProvider(int id) {
        Company owner = companyService.getCurrentCompanyOrThrow();

        Provider entity = providerRepository.findByOwnerCompanyAndIdProvider(owner, id)
                .orElseThrow(ProviderNotFoundException::new);

        entity.setEnabled(false);
        providerRepository.save(entity);
    }

}
