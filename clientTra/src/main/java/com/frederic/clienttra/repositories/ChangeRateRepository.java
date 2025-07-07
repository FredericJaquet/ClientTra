package com.frederic.clienttra.repositories;

import com.frederic.clienttra.entities.ChangeRate;
import com.frederic.clienttra.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChangeRateRepository extends JpaRepository<ChangeRate, Integer> {

    // Busca todos los ChangeRates que pertenezcan a una empresa dueña (ownerCompany) y a la empresa específica idCompany
    List<ChangeRate> findByOwnerCompany(Company ownerCompany);

    // Busca un ChangeRate concreto por ownerCompany, idCompany y idChangeRate
    Optional<ChangeRate> findByOwnerCompanyAndIdChangeRate(Company ownerCompany, Integer idChangeRate);
}
