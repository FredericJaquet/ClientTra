package com.frederic.clienttra.repositories;

import com.frederic.clienttra.entities.Company;
import com.frederic.clienttra.entities.Order;
import com.frederic.clienttra.projections.OrderListForDashboardProjection;
import com.frederic.clienttra.projections.OrderListForDocumentsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
    List<OrderListForDocumentsProjection> findByOwnerCompanyAndCompany_idCompanyOrderByDateOrderDesc(Company owner, Integer idCompany);

    /**
     * Finds all orders for a given owner company and a specific company that have NOT been billed,
     * ordered descending by order date.
     *
     * @param owner the owner company
     * @param idCompany the customer or provider company ID
     * @return list of non-billed order projections ordered by order date descending
     */
    List<OrderListForDocumentsProjection> findByOwnerCompanyAndCompany_idCompanyAndBilledFalseOrderByDateOrderDesc(Company owner, Integer idCompany);

    /**
     * Finds all orders for a given owner company that have NOT been billed,
     * ordered descending by order date.
     *
     * @param owner the owner company
     * @return list of non-billed order projections ordered by order date descending
     */
    List<OrderListForDocumentsProjection> findByOwnerCompanyAndBilledFalseOrderByDateOrderDesc(Company owner);


    @Query("""
        SELECT
            o.idOrder as idOrder,
            o.descrip as descrip,
            o.dateOrder as dateOrder,
            o.total as total,
            o.billed as billed
        FROM Order o
        JOIN Provider prov ON prov.company = o.company AND prov.ownerCompany = :owner
    WHERE o.ownerCompany = :owner
    ORDER BY o.dateOrder DESC
    """)
    List<OrderListForDashboardProjection> findByOwnerCompanyOrdersForProvidersByDateOrderDesc(Company owner);

    @Query("""
        SELECT
            o.idOrder as idOrder,
            o.descrip as descrip,
            o.dateOrder as dateOrder,
            o.total as total,
            o.billed as billed,
            o.company.comName as comName
        FROM Order o
        JOIN Customer cust ON cust.company = o.company AND cust.ownerCompany = :owner
        WHERE o.ownerCompany = :owner AND cust.enabled = true
        ORDER BY o.dateOrder DESC
    """)
    List<OrderListForDashboardProjection> findByOwnerCompanyOrdersForCustomersByDateOrderDesc(Company owner);

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
