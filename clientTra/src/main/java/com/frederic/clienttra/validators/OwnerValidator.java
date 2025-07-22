package com.frederic.clienttra.validators;

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

        // Si es empresa propietaria, debe ser igual al owner
        if (company.getOwnerCompany() == null) {
            if (!company.getIdCompany().equals(owner.getIdCompany())) {
                throw new CompanyNotFoundException();
            }
        }else if (!company.getOwnerCompany().getIdCompany().equals(owner.getIdCompany())) {
            throw new CompanyNotFoundException();
        }
    }

    public Optional<Company> getOwnerCompany(Integer idCompany){
        checkOwner(idCompany);
        return companyService.getCurrentCompany();
    }
}
