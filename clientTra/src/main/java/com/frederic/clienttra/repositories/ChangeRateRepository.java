package com.frederic.clienttra.repositories;

import com.frederic.clienttra.entities.ChangeRate;
import com.frederic.clienttra.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link ChangeRate} entities.
 * <p>
 * Provides methods to retrieve currency exchange rates associated with a specific owner company.
 */
@Repository
public interface ChangeRateRepository extends JpaRepository<ChangeRate, Integer> {

    /**
     * Retrieves all exchange rates registered by the given owner company.
     *
     * @param ownerCompany the company that owns the exchange rates
     * @return list of exchange rates
     */
    List<ChangeRate> findByOwnerCompany(Company ownerCompany);

    /**
     * Retrieves a specific exchange rate by its ID and owner company.
     *
     * @param ownerCompany the company that owns the exchange rate
     * @param idChangeRate the ID of the exchange rate
     * @return optional containing the exchange rate if found
     */
    Optional<ChangeRate> findByOwnerCompanyAndIdChangeRate(Company ownerCompany, Integer idChangeRate);
}
