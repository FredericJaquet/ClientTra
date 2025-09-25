package com.frederic.clienttra.repositories;

import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.entities.Customer;
import com.frederic.clienttra.projections.CustomerListProjection;
import com.frederic.clienttra.projections.CustomerMinimalProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Customer} entities.
 * <p>
 * Provides methods to perform CRUD operations and custom queries to retrieve
 * customers associated with a particular owning company, including projections
 * for listing and minimal data views.
 */
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    /**
     * Finds a customer by its ID and the owning company.
     *
     * @param ownerCompany the company that owns the customer
     * @param id the customer ID
     * @return an Optional containing the customer if found
     */
    Optional<Customer> findByOwnerCompanyAndIdCustomer(Company ownerCompany, Integer id);

    /**
     * Finds a customer by its IDCompany and the owning company.
     *
     * @param ownerCompany the company that owns the customer
     * @param idCompany the Customer's company ID
     * @return an Optional containing the customer if found
     */
    Optional<Customer> findByOwnerCompanyAndCompany_IdCompany(Company ownerCompany, Integer idCompany);

    /**
     * Finds a customer by the owning company and the associated company entity.
     *
     * @param ownerCompany the company that owns the customer
     * @param company the associated company of the customer
     * @return an Optional containing the customer if found
     */
    Optional<Customer> findByOwnerCompanyAndCompany(Company ownerCompany, Company company);

    /**
     * Retrieves all customers owned by a specific company.
     *
     * @param ownerCompany the owning company
     * @return list of customers owned by the company
     */
    List<Customer> findAllByOwnerCompany(Company ownerCompany);

    /**
     * Retrieves a list of customers with selected fields filtered by owning company and enabled status.
     *
     * @param owner the owning company
     * @param enabled filter by enabled status (true/false)
     * @return list of customer projections with basic info
     */
    @Query("""
        SELECT c.idCustomer AS idCustomer,
               co.comName AS comName,
               co.vatNumber AS vatNumber,
               co.email AS email,
               co.web AS web,
               c.enabled AS enabled,
               co.idCompany AS idCompany
        FROM Customer c
        JOIN c.company co
        WHERE c.ownerCompany = :owner AND c.enabled = :enabled
    """)
    List<CustomerListProjection> findListByOwnerCompany(@Param("owner") Company owner, @Param("enabled") Boolean enabled);

    /**
     * Retrieves a list of customers with selected fields filtered by owning company.
     *
     * @param owner the owning company
     * @return list of customer projections with basic info
     */
    @Query("""
        SELECT c.idCustomer AS idCustomer,
               co.comName AS comName,
               co.vatNumber AS vatNumber,
               co.email AS email,
               co.web AS web,
               c.enabled AS enabled,
               co.idCompany AS idCompany
        FROM Customer c
        JOIN c.company co
        WHERE c.ownerCompany = :owner
    """)
    List<CustomerListProjection> findListByOwnerCompany(@Param("owner") Company owner);

    /**
     * Retrieves a list of customers filtered by owning company and matching
     * a search input in company commercial name, legal name, or VAT number (case-insensitive).
     *
     * @param owner the owning company
     * @param input the search string with wildcards (e.g. '%abc%')
     * @return list of customer projections matching the search criteria
     */
    @Query("""
        SELECT c.idCustomer AS idCustomer,
               co.comName AS comName,
               co.legalName,
               co.vatNumber AS vatNumber,
               co.email AS email,
               co.web AS web,
               c.enabled AS enabled,
               co.idCompany AS idCompany
        FROM Customer c
        JOIN c.company co
        WHERE c.ownerCompany = :owner AND (LOWER(co.comName) LIKE LOWER(:input) OR LOWER(co.legalName) LIKE LOWER(:input) OR LOWER(co.vatNumber) LIKE LOWER(:input))
    """)
    List<CustomerListProjection> findListByComNameOrLegalNameOrVatNumber(@Param("owner") Company owner, @Param("input") String input);

    /**
     * Retrieves a minimal list of customers (ID, commercial name, VAT) owned by a company,
     * filtering only enabled customers.
     *
     * @param owner the owning company
     * @return list of minimal customer projections
     */
    @Query("""
    SELECT co.idCompany AS idCompany,
           co.comName AS comName,
           co.vatNumber AS vatNumber
    FROM Customer c
    JOIN c.company co
    WHERE c.ownerCompany = :owner AND c.enabled = true
    """)
    List<CustomerMinimalProjection> findMinimalListByOwnerCompany(Company owner);
}
