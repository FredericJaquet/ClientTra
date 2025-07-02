package com.frederic.clienttra.repositories;

import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.entities.Scheme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SchemeRepository extends JpaRepository<Scheme, Integer> {
    List<Scheme> findByOwnerCompanyAndCompany_idCompany(Company ownerCompany, Integer idCompany);
    Optional<Scheme> findByOwnerCompanyAndIdScheme(Company owner, Integer idScheme);
}
