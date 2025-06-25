package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.UpdateCompanyOwnerDTO;
import com.frederic.clienttra.entities.Company;

import java.util.Optional;

public interface CompanyService {
    Optional<Company> getCurrentCompany();
    Company getCurrentCompanyOrThrow();
    void updateCompanyOwner(UpdateCompanyOwnerDTO dto);
}
