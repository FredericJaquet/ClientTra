package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.create.CreateChangeRateRequestDTO;
import com.frederic.clienttra.dto.read.ChangeRateDTO;
import com.frederic.clienttra.dto.update.UpdateChangeRateRequestDTO;
import com.frederic.clienttra.entities.ChangeRate;
import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.exceptions.AccessDeniedException;
import com.frederic.clienttra.exceptions.ChangeRateNotFoundException;
import com.frederic.clienttra.mappers.ChangeRateMapper;
import com.frederic.clienttra.repositories.ChangeRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing currency exchange rates.
 * <p>
 * Provides methods to list, retrieve, create, update, and delete exchange rates,
 * while enforcing ownership and access control based on the current user's company.
 */
@Service
@RequiredArgsConstructor
public class ChangeRateService {

    private final CompanyService companyService;
    private final ChangeRateRepository changeRateRepository;
    private final ChangeRateMapper changeRateMapper;

    /**
     * Retrieves all exchange rates for the current user's company.
     *
     * @return a list of {@link ChangeRateDTO} objects
     */
    @Transactional(readOnly = true)
    public List<ChangeRateDTO> getAllChangeRates() {
        Company owner = companyService.getCurrentCompanyOrThrow();
        return changeRateMapper.toDtos(changeRateRepository.findByOwnerCompany(owner));
    }

    /**
     * Retrieves a specific exchange rate by its ID for the current user's company.
     *
     * @param idChangeRate the ID of the exchange rate
     * @return the {@link ChangeRateDTO} corresponding to the given ID
     * @throws ChangeRateNotFoundException if no exchange rate with the given ID exists for the current company
     */
    @Transactional(readOnly = true)
    public ChangeRateDTO getChangeRateById(Integer idChangeRate) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        ChangeRate entity = changeRateRepository.findByOwnerCompanyAndIdChangeRate(owner, idChangeRate)
                .orElseThrow(ChangeRateNotFoundException::new);
        return changeRateMapper.toDto(entity);
    }

    /**
     * Retrieves the {@link ChangeRate} entity by ID and validates ownership.
     *
     * @param idChangeRate the ID of the exchange rate
     * @param owner the company owner to validate
     * @return the {@link ChangeRate} entity
     * @throws AccessDeniedException if the provided owner does not match the current user's company
     * @throws ChangeRateNotFoundException if no exchange rate with the given ID exists for the owner
     */
    @Transactional
    public ChangeRate getChangeRateByIdAndOwner(Integer idChangeRate, Company owner){
        Company currentOwner = companyService.getCurrentCompanyOrThrow();
        if (!owner.equals(currentOwner)) {
            throw new AccessDeniedException();
        }

        return changeRateRepository.findByOwnerCompanyAndIdChangeRate(owner, idChangeRate)
                .orElseThrow(ChangeRateNotFoundException::new);
    }

    /**
     * Creates a new exchange rate for the current user's company.
     *
     * @param dto the data transfer object containing the details for the new exchange rate
     * @return the created {@link ChangeRateDTO}
     */
    @Transactional
    public ChangeRateDTO createChangeRate(CreateChangeRateRequestDTO dto) {
        Company owner = companyService.getCurrentCompanyOrThrow();

        ChangeRate entity = changeRateMapper.toEntity(dto);
        entity.setOwnerCompany(owner);
        changeRateRepository.save(entity);
        return changeRateMapper.toDto(entity);
    }

    /**
     * Updates an existing exchange rate identified by ID for the current user's company.
     *
     * @param idChangeRate the ID of the exchange rate to update
     * @param dto the data transfer object containing updated fields
     * @return the updated {@link ChangeRateDTO}
     * @throws ChangeRateNotFoundException if the exchange rate does not exist for the current company
     */
    @Transactional
    public ChangeRateDTO updateChangeRate(Integer idChangeRate, UpdateChangeRateRequestDTO dto) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        ChangeRate entity = changeRateRepository.findByOwnerCompanyAndIdChangeRate(owner, idChangeRate)
                .orElseThrow(ChangeRateNotFoundException::new);

        changeRateMapper.updateEntity(entity, dto);
        changeRateRepository.save(entity);
        return changeRateMapper.toDto(entity);
    }

    /**
     * Deletes an exchange rate by ID for the current user's company.
     *
     * @param idChangeRate the ID of the exchange rate to delete
     * @throws ChangeRateNotFoundException if the exchange rate does not exist for the current company
     */
    @Transactional
    public void deleteChangeRate(Integer idChangeRate) {
        Company owner = companyService.getCurrentCompanyOrThrow();
        ChangeRate entity = changeRateRepository.findByOwnerCompanyAndIdChangeRate(owner, idChangeRate)
                .orElseThrow(ChangeRateNotFoundException::new);
        changeRateRepository.delete(entity);
    }
}
