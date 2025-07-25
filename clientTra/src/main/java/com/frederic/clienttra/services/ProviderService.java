package com.frederic.clienttra.services;


import com.frederic.clienttra.dto.create.CreateProviderRequestDTO;
import com.frederic.clienttra.dto.read.*;
import com.frederic.clienttra.dto.update.UpdateProviderRequestDTO;

import java.util.List;

public interface ProviderService {
    List<ProviderForListDTO> getAllProviders();
    List<ProviderForListDTO> getAllProvidersEnabled(boolean enabled);
    ProviderDetailsDTO getProviderById(int id);
    List<ProviderForListDTO> searchByNameOrVat(String query);
    List<BaseCompanyMinimalDTO> getMinimalProviderList();
    int createProvider(CreateProviderRequestDTO dto);
    void updateProvider(int id, UpdateProviderRequestDTO dto);
    void disableProvider(int id);
}
