package com.frederic.clienttra.utils.validators;

import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.exceptions.AccessDeniedException;
import com.frederic.clienttra.exceptions.CompanyNotFoundException;
import com.frederic.clienttra.repositories.CompanyRepository;
import com.frederic.clienttra.services.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OwnerValidator {

    private final CompanyService companyService;
    private final CompanyRepository companyRepository;

    public void checkOwner(Integer idCompany){
        Company owner = companyService.getCurrentCompanyOrThrow();
        Company company = companyRepository.findByIdCompany(idCompany)
                .orElseThrow(CompanyNotFoundException::new);
        if (!company.getOwnerCompany().equals(owner)) {
            throw new AccessDeniedException();
        }
    }

    public Optional<Company> getOwnerCompany(Integer idCompany){
        checkOwner(idCompany);
        return companyService.getCurrentCompany();
    }
}
