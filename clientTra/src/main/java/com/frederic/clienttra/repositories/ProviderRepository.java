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

public interface ProviderRepository extends JpaRepository<Provider, Integer> {
    Optional<Provider> findByOwnerCompanyAndIdProvider(Company ownerCompany, Integer id);
    List<ProviderListProjection> findByOwnerCompanyAndEnabled(Company ownerCompany, boolean enabled);

    @Query("""
        SELECT p.idProvider AS idProvider,
               co.comName AS comName,
               co.vatNumber AS vatNumber,
               co.email AS email,
               co.web AS web,
               p.enabled AS enabled
        FROM Provider p
        JOIN p.company co
        WHERE p.ownerCompany = :owner AND p.enabled = true
    """)
    List<ProviderListProjection> findListByOwnerCompany(@Param("owner") Company owner);

    @Query("""
        SELECT p.idProvider AS idProvider,
               co.comName AS comName,
               co.legalName,
               co.vatNumber AS vatNumber,
               co.email AS email,
               co.web AS web,
               p.enabled AS enabled
        FROM Provider p
        JOIN p.company co
        WHERE p.ownerCompany = :owner AND (LOWER(co.comName) LIKE LOWER(:input) OR LOWER(co.legalName) LIKE LOWER(:input) OR LOWER(co.vatNumber) LIKE LOWER(:input))
    """)
    List<ProviderListProjection> findListByComNameOrLegalNameOrVatNumber(@Param("owner") Company owner, @Param("input") String input);

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
