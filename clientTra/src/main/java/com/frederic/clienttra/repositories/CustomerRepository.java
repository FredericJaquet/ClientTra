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
    Optional<Customer> findByOwnerCompanyAndIdCustomer(Company ownerCompany, Integer id);
    Optional<Customer> findByOwnerCompanyAndCompany(Company ownerCompany, Company company);
    List<Customer> findAllByOwnerCompany(Company ownerCompany);

    @Query("""
        SELECT c.idCustomer AS idCustomer,
               co.comName AS comName,
               co.vatNumber AS vatNumber,
               co.email AS email,
               co.web AS web,
               c.enabled AS enabled
        FROM Customer c
        JOIN c.company co
        WHERE c.ownerCompany = :owner AND c.enabled = :enabled
    """)
    List<CustomerListProjection> findListByOwnerCompany(@Param("owner") Company owner, @Param("enabled") Boolean enabled);

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
    SELECT co.idCompany AS idCompany,
           co.comName AS comName,
           co.vatNumber AS vatNumber
    FROM Customer c
    JOIN c.company co
    WHERE c.ownerCompany = :owner AND c.enabled = true
""")
    List<CustomerMinimalProjection> findMinimalListByOwnerCompany(Company owner);

}
