package com.frederic.clienttra.controllers;

import com.frederic.clienttra.dto.GenericResponseDTO;
import com.frederic.clienttra.dto.create.CreateOrderRequestDTO;
import com.frederic.clienttra.dto.read.OrderDetailsDTO;
import com.frederic.clienttra.dto.read.OrderListDTO;
import com.frederic.clienttra.dto.update.UpdateOrderRequestDTO;
import com.frederic.clienttra.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing orders related to companies.
 * <p>
 * Provides endpoints to list all orders, list pending orders,
 * retrieve order details, create, update, and delete orders.
 * Modifications require ADMIN or ACCOUNTING roles.
 */
@RestController
@RequestMapping("/api/companies/{idCompany}/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * Retrieves all orders for a given company.
     *
     * @param idCompany the company ID
     * @return a list of {@link OrderListDTO}
     */
    @GetMapping
    public ResponseEntity<List<OrderListDTO>> getOrders(@PathVariable Integer idCompany) {
        List<OrderListDTO> orders = orderService.getOrders(idCompany);
        return ResponseEntity.ok(orders);
    }

    /**
     * Retrieves pending orders for a given company.
     * Restricted to users with ADMIN or ACCOUNTING roles.
     *
     * @param idCompany the company ID
     * @return a list of pending {@link OrderListDTO}
     */
    @GetMapping("/pending")
    public ResponseEntity<List<OrderListDTO>> getPendingOrders(@PathVariable Integer idCompany) {
        List<OrderListDTO> orders = orderService.getPendingOrders(idCompany);
        return ResponseEntity.ok(orders);
    }

    /**
     * Retrieves details of a specific order by ID for a given company.
     *
     * @param idCompany the company ID
     * @param idOrder   the order ID
     * @return the {@link OrderDetailsDTO} of the requested order
     */
    @GetMapping("/{idOrder}")
    public ResponseEntity<OrderDetailsDTO> getOrder(@PathVariable Integer idCompany,
                                                    @PathVariable Integer idOrder) {
        OrderDetailsDTO order = orderService.getOrderDetails(idCompany, idOrder);
        return ResponseEntity.ok(order);
    }

    /**
     * Creates a new order for the specified company.
     * Restricted to users with ADMIN or ACCOUNTING roles.
     *
     * @param idCompany the company ID
     * @param dto       the order creation request payload
     * @return a success message wrapped in {@link GenericResponseDTO}
     */
    @PostMapping
    public ResponseEntity<OrderDetailsDTO> createOrder(@PathVariable Integer idCompany,
                                                          @Valid @RequestBody CreateOrderRequestDTO dto) {
        OrderDetailsDTO newDto=orderService.createOrder(idCompany, dto);
        return ResponseEntity.ok(newDto);
    }

    /**
     * Updates an existing order for the specified company.
     * Restricted to users with ADMIN or ACCOUNTING roles.
     *
     * @param idCompany the company ID
     * @param idOrder   the order ID
     * @param dto       the order update payload
     * @return the updated {@link OrderDetailsDTO}
     */
    @PatchMapping("/{idOrder}")
    public ResponseEntity<OrderDetailsDTO> updateOrder(@PathVariable Integer idCompany,
                                                       @PathVariable Integer idOrder,
                                                       @Valid @RequestBody UpdateOrderRequestDTO dto) {
        OrderDetailsDTO updated = orderService.updateOrder(idCompany, idOrder, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Deletes (soft delete) an order for the specified company.
     * Restricted to users with ADMIN or ACCOUNTING roles.
     *
     * @param idCompany the company ID
     * @param idOrder   the order ID
     * @return a success message wrapped in {@link GenericResponseDTO}
     */
    @DeleteMapping("/{idOrder}")
    public ResponseEntity<GenericResponseDTO> deleteOrder(@PathVariable Integer idCompany,
                                                          @PathVariable Integer idOrder) {
        orderService.deleteOrder(idCompany, idOrder);
        return ResponseEntity.ok(new GenericResponseDTO("order.deleted.success"));
    }
}
