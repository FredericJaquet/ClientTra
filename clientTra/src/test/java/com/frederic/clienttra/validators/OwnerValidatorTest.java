package com.frederic.clienttra.validators;

import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.exceptions.CompanyNotFoundException;
import com.frederic.clienttra.repositories.CompanyRepository;
import com.frederic.clienttra.services.CompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class OwnerValidatorTest {

    private OwnerValidator ownerValidator;
    private CompanyService companyService;
    private CompanyRepository companyRepository;

    private Company owner;
    private Company secondary;

    @BeforeEach
    void setUp() {
        companyService = mock(CompanyService.class);
        companyRepository = mock(CompanyRepository.class);
        ownerValidator = new OwnerValidator(companyService, companyRepository);

        owner = new Company();
        owner.setIdCompany(1);

        secondary = new Company();
        secondary.setIdCompany(2);
        secondary.setOwnerCompany(owner);
    }

    @Test
    void checkOwner_withSecondaryCompanyOfOwner_shouldPass() {
        when(companyService.getCurrentCompanyOrThrow()).thenReturn(owner);
        when(companyRepository.findByIdCompany(2)).thenReturn(Optional.of(secondary));

        ownerValidator.checkOwner(2); // no lanza excepción
    }

    @Test
    void checkOwner_withOwnerCompany_itself_shouldPass() {
        when(companyService.getCurrentCompanyOrThrow()).thenReturn(owner);
        when(companyRepository.findByIdCompany(1)).thenReturn(Optional.of(owner));

        ownerValidator.checkOwner(1); // no lanza excepción
    }

    @Test
    void checkOwner_withCompanyNotBelongingToOwner_shouldThrowException() {
        Company strangerCompany = new Company();
        strangerCompany.setIdCompany(3);
        strangerCompany.setOwnerCompany(new Company()); // otra empresa

        when(companyService.getCurrentCompanyOrThrow()).thenReturn(owner);
        when(companyRepository.findByIdCompany(3)).thenReturn(Optional.of(strangerCompany));

        assertThatThrownBy(() -> ownerValidator.checkOwner(3))
                .isInstanceOf(CompanyNotFoundException.class);
    }

    @Test
    void checkOwner_withNonExistingCompany_shouldThrowException() {
        when(companyService.getCurrentCompanyOrThrow()).thenReturn(owner);
        when(companyRepository.findByIdCompany(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> ownerValidator.checkOwner(99))
                .isInstanceOf(CompanyNotFoundException.class);
    }
}