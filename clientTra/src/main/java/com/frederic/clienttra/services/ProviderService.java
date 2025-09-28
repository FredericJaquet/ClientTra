package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.create.CreateProviderRequestDTO;
import com.frederic.clienttra.dto.read.*;
import com.frederic.clienttra.dto.update.UpdateProviderRequestDTO;

import java.util.List;

/**
 * Service interface for managing providers.
 */
public interface ProviderService {

    /**
     * Retrieves all providers regardless of their enabled status.
     *
     * @return list of all providers.
     */
    List<ProviderForListDTO> getAllProviders();

    /**
     * Retrieves all providers filtered by enabled/disabled status.
     *
     * @param enabled true to get only enabled providers, false for disabled.
     * @return list of filtered providers.
     */
    List<ProviderForListDTO> getAllProvidersEnabled(boolean enabled);

    /**
     * Retrieves provider details by provider ID.
     *
     * @param id the provider ID.
     * @return provider details DTO.
     */
    ProviderDetailsDTO getProviderByIdCompany(int id);

    /**
     * Searches providers by name or VAT number matching the query.
     *
     * @param query search string for name or VAT.
     * @return list of matching providers.
     */
    List<ProviderForListDTO> searchByNameOrVat(String query);

    /**
     * Retrieves a minimal list of providers with only essential company data.
     *
     * @return list of minimal provider company DTOs.
     */
    List<BaseCompanyMinimalDTO> getMinimalProviderList();

    /**
     * Creates a new provider.
     *
     * @param dto the provider creation DTO.
     * @return the generated ID of the created provider.
     */
    ProviderForListDTO createProvider(CreateProviderRequestDTO dto);

    /**
     * Updates an existing provider identified by its ID.
     *
     * @param id the provider ID.
     * @param dto the update provider DTO.
     */
    void updateProvider(int id, UpdateProviderRequestDTO dto);

    /**
     * Disables a provider by marking it as inactive.
     *
     * @param id the provider ID.
     */
    void disableProvider(int id);
}
