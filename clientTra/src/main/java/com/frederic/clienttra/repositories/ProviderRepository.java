package com.frederic.clienttra.repositories;

import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.entities.Provider;
import com.frederic.clienttra.projections.ProviderListProjection;
import com.frederic.clienttra.projections.ProviderMinimalProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Provider} entities.
 * <p>
 * Extends JpaRepository to provide basic CRUD operations and
 * defines custom queries for listing providers with projections.
 */
public interface ProviderRepository extends JpaRepository<Provider, Integer> {

    /**
     * Finds a provider by its ID and owner company.
     *
     * @param ownerCompany the owning company
     * @param id the provider ID
     * @return an Optional containing the Provider if found, or empty otherwise
     */
    Optional<Provider> findByOwnerCompanyAndIdProvider(Company ownerCompany, Integer id);

    /**
     * Finds a provider by its ID and owner company.
     *
     * @param ownerCompany the owning company
     * @param idCompany provider's company ID
     * @return an Optional containing the Provider if found, or empty otherwise
     */
    Optional<Provider> findByOwnerCompanyAndCompany_IdCompany(Company ownerCompany, Integer idCompany);

    /**
     * Finds a provider by its owning company and associated company entity.
     *
     * @param ownerCompany the owning company
     * @param company the associated company entity
     * @return an Optional containing the Provider if found, or empty otherwise
     */
    Optional<Provider> findByOwnerCompanyAndCompany(Company ownerCompany, Company company);

    /**
     * Retrieves a list of providers for an owner company filtered by enabled status.
     * Returns a projection with basic provider information.
     *
     * @param owner the owning company
     * @param enabled the enabled status filter
     * @return a list of providers matching the criteria
     */
    @Query("""
        SELECT p.idProvider AS idProvider,
               co.comName AS comName,
               co.vatNumber AS vatNumber,
               co.email AS email,
               co.web AS web,
               p.enabled AS enabled,
               co.idCompany AS idCompany
        FROM Provider p
        JOIN p.company co
        WHERE p.ownerCompany = :owner AND p.enabled = :enabled
    """)
    List<ProviderListProjection> findListByOwnerCompany(@Param("owner") Company owner, @Param("enabled") boolean enabled);

    /**
     * Retrieves a list of providers for an owner company.
     * Returns a projection with basic provider information.
     *
     * @param owner the owning company
     * @return a list of providers matching the criteria
     */
    @Query("""
        SELECT p.idProvider AS idProvider,
               co.comName AS comName,
               co.vatNumber AS vatNumber,
               co.email AS email,
               co.web AS web,
               p.enabled AS enabled,
               co.idCompany AS idCompany
        FROM Provider p
        JOIN p.company co
        WHERE p.ownerCompany = :owner
    """)
    List<ProviderListProjection> findListByOwnerCompany(@Param("owner") Company owner);

    /**
     * Retrieves a list of providers filtered by company name, legal name or VAT number,
     * performing a case-insensitive partial match.
     *
     * @param owner the owning company
     * @param input the search input string (should contain % for wildcards)
     * @return a list of providers matching the search criteria
     */
    @Query("""
        SELECT p.idProvider AS idProvider,
               co.comName AS comName,
               co.legalName,
               co.vatNumber AS vatNumber,
               co.email AS email,
               co.web AS web,
               p.enabled AS enabled,
               co.idCompany AS idCompany
        FROM Provider p
        JOIN p.company co
        WHERE p.ownerCompany = :owner AND (LOWER(co.comName) LIKE LOWER(:input) OR LOWER(co.legalName) LIKE LOWER(:input) OR LOWER(co.vatNumber) LIKE LOWER(:input))
    """)
    List<ProviderListProjection> findListByComNameOrLegalNameOrVatNumber(@Param("owner") Company owner, @Param("input") String input);

    /**
     * Retrieves a minimal list of enabled providers for an owner company,
     * returning only company ID, commercial name and VAT number.
     *
     * @param owner the owning company
     * @return a list of minimal provider projections
     */
    @Query("""
        SELECT co.idCompany AS idCompany,
               co.comName AS comName,
               co.vatNumber AS vatNumber
        FROM Provider p
        JOIN p.company co
        WHERE p.ownerCompany = :owner AND p.enabled = true
    """)
    List<ProviderMinimalProjection> findMinimalListByOwnerCompany(Company owner);
}
