package com.frederic.clienttra.services;

import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.exceptions.CompanyNotFoundForUserException;
import com.frederic.clienttra.repositories.CompanyRepository;
import com.frederic.clienttra.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public Optional<Company> getCurrentCompany() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            int currentCompanyId = userDetails.getIdCompany();
            return companyRepository.findById(currentCompanyId);
        }

        return Optional.empty();
    }

    @Override
    public Company getCurrentCompanyOrThrow() {
        return getCurrentCompany().orElseThrow(CompanyNotFoundForUserException::new);
    }
}
