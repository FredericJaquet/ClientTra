package com.frederic.clienttra.controllers;


import com.frederic.clienttra.dto.read.OrderListDTO;
import com.frederic.clienttra.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for managing orders NOT related to companies.
 * <p>
 * Provides endpoints to list all orders, list pending orders.
 * Modifications require ADMIN or ACCOUNTING roles.
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderGlobalController {

    private final OrderService orderService;

    /**
     * Retrieves all pending orders.
     * Restricted to users with ADMIN or ACCOUNTING roles.
     *
     * @return a list of pending {@link OrderListDTO}
     */
    @GetMapping("/pending")
    public ResponseEntity<List<OrderListDTO>> getPendingOrders() {
        List<OrderListDTO> orders = orderService.getPendingOrders();
        return ResponseEntity.ok(orders);
    }
}
