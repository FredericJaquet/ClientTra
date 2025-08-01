package com.frederic.clienttra.repositories;

import com.frederic.clienttra.entities.Company;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for accessing and managing {@link Company} entities.
 * <p>
 * Supports querying companies by VAT number, name, ID, and ownership relationships.
 */
public interface CompanyRepository extends JpaRepository<Company, Integer> {

    /**
     * Checks if a company with the given VAT number exists, and is not owned by any other company.
     * Typically used to detect duplicates in base company registration (e.g. customers or providers).
     *
     * @param vatNumber the VAT number to check
     * @return true if such a company exists, false otherwise
     */
    boolean existsByVatNumberAndOwnerCompanyIsNull(@NotBlank String vatNumber);

    /**
     * Finds a company by its owner and commercial name.
     *
     * @param ownerCompany the owning company (e.g. current user's company)
     * @param comName the commercial name to search
     * @return optional containing the found company, or empty if not found
     */
    Optional<Company> findByOwnerCompanyAndComName(Company ownerCompany, String comName);

    /**
     * Finds a company by its owner and VAT number.
     *
     * @param ownerCompany the owning company (e.g. current user's company)
     * @param vatNumber the VAT number to search
     * @return optional containing the found company, or empty if not found
     */
    Optional<Company> findByOwnerCompanyAndVatNumber(Company ownerCompany, String vatNumber);

    /**
     * Finds a company by its ID.
     *
     * @param idCompany the ID of the company
     * @return optional containing the company if found
     */
    Optional<Company> findByIdCompany(Integer idCompany);

    /**
     * Retrieves all companies owned by the given company.
     * This typically returns customers or providers registered by the owner company.
     *
     * @param onwerCompany the owning company
     * @return list of owned companies
     */
    List<Company> findAllByOwnerCompany(Company onwerCompany);

}
