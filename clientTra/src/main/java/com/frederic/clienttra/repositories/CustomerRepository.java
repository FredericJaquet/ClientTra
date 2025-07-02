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

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    List<Customer> findByOwnerCompanyIdCompanyAndEnabledTrue(Integer ownerId);
    List<Customer> findByOwnerCompanyAndEnabledTrue(Company ownerCompany);
    Optional<Customer> findByOwnerCompanyAndIdCustomer(Company ownerCompany, Integer id);

    @Query("""
        SELECT c.idCustomer AS idCustomer,
               co.comName AS comName,
               co.vatNumber AS vatNumber,
               co.email AS email,
               co.web AS web,
               c.enabled AS enabled
        FROM Customer c
        JOIN c.company co
        WHERE c.ownerCompany = :owner AND c.enabled = true
    """)
    List<CustomerListProjection> findListByOwnerCompany(@Param("owner") Company owner);

    @Query("""
        SELECT c.idCustomer AS idCustomer,
               co.comName AS comName,
               co.legalName,
               co.vatNumber AS vatNumber,
               co.email AS email,
               co.web AS web,
               c.enabled AS enabled
        FROM Customer c
        JOIN c.company co
        WHERE c.ownerCompany = :owner AND (LOWER(co.comName) LIKE LOWER(:input) OR LOWER(co.legalName) LIKE LOWER(:input) OR LOWER(co.vatNumber) LIKE LOWER(:input))
    """)
    List<CustomerListProjection> findListByComNameOrLegalNameOrVatNumber(@Param("owner") Company owner, @Param("input") String input);

    @Query("""
    SELECT c FROM Customer c
    JOIN FETCH c.company comp
    LEFT JOIN FETCH comp.addresses
    LEFT JOIN FETCH comp.phones
    LEFT JOIN FETCH comp.bankAccounts
    LEFT JOIN FETCH comp.contactPersons
    WHERE c.ownerCompany = :owner AND c.idCustomer = :id
""")
    Optional<Customer> findByOwnerCompanyAndIdCustomerWithDetails(Company owner, int id);

    @Query("""
    SELECT c.idCustomer AS idCustomer,
           c.comName AS comName,
           c.vatNumber AS vatNumber
    FROM Customer c
    WHERE c.ownerCompany = :owner
""")
    List<CustomerMinimalProjection> findMinimalListByOwnerCompany(Company owner);


}
