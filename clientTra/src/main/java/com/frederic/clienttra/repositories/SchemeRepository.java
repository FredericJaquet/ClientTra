package com.frederic.clienttra.repositories;

import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.entities.Scheme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Scheme} entities.
 * <p>
 * Extends JpaRepository to provide CRUD operations and defines
 * custom methods to query schemes based on the owning company and related company.
 */
public interface SchemeRepository extends JpaRepository<Scheme, Integer> {

    /**
     * Finds all schemes associated with a given owner company and a specific company ID.
     *
     * @param ownerCompany the owner company that owns the schemes
     * @param idCompany    the ID of the related company
     * @return list of schemes matching the criteria
     */
    List<Scheme> findByOwnerCompanyAndCompany_idCompany(Company ownerCompany, Integer idCompany);

    /**
     * Finds all schemes owned by a given company.
     *
     * @param ownerCompany the company owning the schemes
     * @return list of schemes owned by the company
     */
    List<Scheme> findAllByOwnerCompany(Company ownerCompany);

    /**
     * Finds a specific scheme by its ID and owner company.
     *
     * @param owner    the owner company
     * @param idScheme the ID of the scheme
     * @return optional scheme entity if found
     */
    Optional<Scheme> findByOwnerCompanyAndIdScheme(Company owner, Integer idScheme);

}
