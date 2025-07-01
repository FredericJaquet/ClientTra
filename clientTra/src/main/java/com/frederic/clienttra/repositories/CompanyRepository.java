package com.frederic.clienttra.repositories;

import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.entities.Customer;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
    boolean existsByVatNumberAndOwnerCompanyIsNull(@NotBlank String vatNumber);
    Optional<Company> findByOwnerCompanyAndComName(Company ownerCompany, String comName);
    Optional<Company> findByOwnerCompanyAndVatNumber(Company ownerCompany, String vatNumber);




}
