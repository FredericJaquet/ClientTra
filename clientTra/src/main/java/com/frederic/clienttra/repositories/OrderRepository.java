package com.frederic.clienttra.repositories;

import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.entities.Order;
import com.frederic.clienttra.projections.OrderListProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Order} entities.
 * <p>
 * Extends JpaRepository to provide CRUD operations.
 * Includes methods to query orders by company, billing status, and ownership,
 * with projections for list views.
 */
public interface OrderRepository extends JpaRepository<Order, Integer> {

    /**
     * Finds all orders for a given owner company and a specific customer/provider company,
     * ordered descending by order date.
     *
     * @param owner the owner company
     * @param idCompany the customer or provider company ID
     * @return list of order projections with summary data ordered by order date descending
     */
    List<OrderListProjection> findByOwnerCompanyAndCompany_idCompanyOrderByDateOrderDesc(Company owner, Integer idCompany);

    /**
     * Finds all orders for a given owner company and a specific company that have NOT been billed,
     * ordered descending by order date.
     *
     * @param owner the owner company
     * @param idCompany the customer or provider company ID
     * @return list of non-billed order projections ordered by order date descending
     */
    List<OrderListProjection> findByOwnerCompanyAndCompany_idCompanyAndBilledFalseOrderByDateOrderDesc(Company owner, Integer idCompany);

    /**
     * Finds an order by its ID and owner company.
     *
     * @param idOrder the order ID
     * @param ownerCompany the owner company
     * @return optional containing the order if found
     */
    Optional<Order> findByIdOrderAndOwnerCompany(Integer idOrder, Company ownerCompany);

    /**
     * Finds all billed orders for a given owner company and specific company,
     * ordered descending by order date.
     *
     * @param owner the owner company
     * @param idCompany the customer or provider company ID
     * @return list of billed orders ordered by order date descending
     */
    List<Order> findByOwnerCompanyAndCompany_idCompanyAndBilledTrueOrderByDateOrderDesc(Company owner, Integer idCompany);

    /**
     * Finds all orders by a list of order IDs and owner company.
     *
     * @param ids list of order IDs
     * @param owner the owner company
     * @return list of orders matching the criteria
     */
    List<Order> findAllByIdOrderInAndOwnerCompany(List<Integer> ids, Company owner);

    /**
     * Finds all orders belonging to an owner company.
     *
     * @param ownerCompany the owner company
     * @return list of all orders for the owner company
     */
    List<Order> findAllByOwnerCompany(Company ownerCompany);

}
