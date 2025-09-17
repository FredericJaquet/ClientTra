package com.frederic.clienttra.controllers;


import com.frederic.clienttra.dto.GenericResponseDTO;
import com.frederic.clienttra.dto.read.OrderDetailsDTO;
import com.frederic.clienttra.dto.read.OrderListDTO;
import com.frederic.clienttra.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing orders NOT related to companies.
 * <p>
 * Provides endpoints to list all orders, list pending orders.
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderGlobalController {

    private final OrderService orderService;

    /**
     * Retrieves all pending orders.
     *
     * @return a list of pending {@link OrderListDTO}
     */
    @GetMapping("/pending")
    public ResponseEntity<List<OrderListDTO>> getPendingOrders() {
        List<OrderListDTO> orders = orderService.getPendingOrders();
        return ResponseEntity.ok(orders);
    }

    /**
     * Retrieves all orders of customers.
     *
     * @return a list of pending {@link OrderListDTO}
     */
    @GetMapping("/customers")
    public ResponseEntity<List<OrderListDTO>> getCustomersOrders() {
        List<OrderListDTO> orders = orderService.getCustomersOrders();
        return ResponseEntity.ok(orders);
    }

    /**
     * Retrieves all orders of providers.
     *
     * @return a list of pending {@link OrderListDTO}
     */
    @GetMapping("/providers")
    public ResponseEntity<List<OrderListDTO>> getProvidersOrders() {
        List<OrderListDTO> orders = orderService.getProvidersOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{idOrder}")
    public ResponseEntity<OrderDetailsDTO> getOrder(@PathVariable Integer idOrder){
        OrderDetailsDTO order = orderService.getOrderDetails(idOrder);
        return ResponseEntity.ok(order);
    }

    /**
     * Deletes (soft delete) an order for the specified company.
     * Restricted to users with ADMIN or ACCOUNTING roles.
     *
     *
     * @param idOrder   the order ID
     * @return a success message wrapped in {@link GenericResponseDTO}
     */
    @DeleteMapping("/{idOrder}")
    public ResponseEntity<GenericResponseDTO> deleteOrder(@PathVariable Integer idOrder) {
        orderService.deleteOrder(idOrder);
        return ResponseEntity.ok(new GenericResponseDTO("order.deleted.success"));
    }


}
