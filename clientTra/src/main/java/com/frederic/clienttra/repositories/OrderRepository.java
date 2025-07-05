package com.frederic.clienttra.repositories;

import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findByOwnerCompanyAndCompany_idCompanyOrderByDateOrderDesc(Company owner, Integer idCompany);
    List<Order> findByOwnerCompanyAndCompany_idCompanyAndBilledFalseOrderByDateOrderDesc(Company owner, Integer idCompany);
    Optional<Order> findByIdAndOwnerCompany(Integer idOrder, Company ownerCompany);
    List<Order> findByOwnerCompanyAndCompany_idCompanyAndBilledTrueOrderByDateOrderDesc(Company owner, Integer idCompany);

}