package com.frederic.clienttra.services;

import com.frederic.clienttra.dto.update.UpdateCompanyOwnerRequestDTO;
import com.frederic.clienttra.entities.Company;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface CompanyService {
    Optional<Company> getCurrentCompany();
    Company getCurrentCompanyOrThrow();
    void updateCompanyOwner(UpdateCompanyOwnerRequestDTO dto);
    void uploadCompanyLogo(MultipartFile file);

}
