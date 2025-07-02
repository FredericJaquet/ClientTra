package com.frederic.clienttra.repositories;

import com.frederic.clienttra.entities.Company;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface CompanyRepository extends JpaRepository<Company, Integer> {
    boolean existsByVatNumberAndOwnerCompanyIsNull(@NotBlank String vatNumber);
    Optional<Company> findByOwnerCompanyAndComName(Company ownerCompany, String comName);
    Optional<Company> findByOwnerCompanyAndVatNumber(Company ownerCompany, String vatNumber);
    Optional<Company> findByIdCompany(Integer idCompany);



}
