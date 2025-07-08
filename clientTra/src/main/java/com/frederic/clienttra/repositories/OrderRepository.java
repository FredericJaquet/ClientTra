package com.frederic.clienttra.repositories;

import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.entities.Order;
import com.frederic.clienttra.projections.OrderListProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<OrderListProjection> findByOwnerCompanyAndCompany_idCompanyOrderByDateOrderDesc(Company owner, Integer idCompany);
    List<OrderListProjection> findByOwnerCompanyAndCompany_idCompanyAndBilledFalseOrderByDateOrderDesc(Company owner, Integer idCompany);
    Optional<Order> findByIdOrderAndOwnerCompany(Integer idOrder, Company ownerCompany);
    List<Order> findByOwnerCompanyAndCompany_idCompanyAndBilledTrueOrderByDateOrderDesc(Company owner, Integer idCompany);
    List<Order> findAllByIdOrderInAndOwnerCompany(List<Integer> ids, Company owner);


}