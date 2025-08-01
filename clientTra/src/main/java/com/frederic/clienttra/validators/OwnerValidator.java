package com.frederic.clienttra.validators;

import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.exceptions.CompanyNotFoundException;
import com.frederic.clienttra.repositories.CompanyRepository;
import com.frederic.clienttra.services.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Validator to check ownership of a company entity.
 * Ensures that the requested company belongs to the current user's owner company.
 */
@Component
@RequiredArgsConstructor
public class OwnerValidator {

    private final CompanyService companyService;
    private final CompanyRepository companyRepository;

    /**
     * Checks if the company with the given ID belongs to the current owner company.
     *
     * @param idCompany the ID of the company to check
     * @throws CompanyNotFoundException if the company does not belong to the current owner
     */
    public void checkOwner(Integer idCompany){
        Company owner = companyService.getCurrentCompanyOrThrow();
        Company company = companyRepository.findByIdCompany(idCompany)
                .orElseThrow(CompanyNotFoundException::new);

        // If the company is a root company (ownerCompany == null),
        // then the company's own ID must match the current owner company ID
        if (company.getOwnerCompany() == null) {
            if (!company.getIdCompany().equals(owner.getIdCompany())) {
                throw new CompanyNotFoundException();
            }
        } else if (!company.getOwnerCompany().getIdCompany().equals(owner.getIdCompany())) {
            throw new CompanyNotFoundException();
        }
    }

    /**
     * Returns the current owner company if the given company ID passes the ownership check.
     *
     * @param idCompany the ID of the company to check ownership for
     * @return the current owner company wrapped in an Optional
     * @throws CompanyNotFoundException if the company does not belong to the current owner
     */
    public Optional<Company> getOwnerCompany(Integer idCompany){
        checkOwner(idCompany);
        return companyService.getCurrentCompany();
    }
}
