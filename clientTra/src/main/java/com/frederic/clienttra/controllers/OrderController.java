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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies/{idCompany}/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderListDTO>> getOrders(@PathVariable Integer idCompany) {
        List<OrderListDTO> orders = orderService.getOrders(idCompany);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<List<OrderListDTO>> getPendingOrders(@PathVariable Integer idCompany) {
        List<OrderListDTO> orders = orderService.getPendingOrders(idCompany);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{idOrder}")
    public ResponseEntity<OrderDetailsDTO> getOrder(@PathVariable Integer idCompany,
                                                    @PathVariable Integer idOrder) {
        OrderDetailsDTO order = orderService.getOrderDetails(idCompany, idOrder);
        return ResponseEntity.ok(order);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<GenericResponseDTO> createOrder(@PathVariable Integer idCompany,
                                                          @Valid @RequestBody CreateOrderRequestDTO dto) {
        orderService.createOrder(idCompany, dto);
        return ResponseEntity.ok(new GenericResponseDTO("order.created.success"));
    }

    @PatchMapping("/{idOrder}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<OrderDetailsDTO> updateOrder(@PathVariable Integer idCompany,
                                                       @PathVariable Integer idOrder,
                                                       @Valid @RequestBody UpdateOrderRequestDTO dto) {
        OrderDetailsDTO updated = orderService.updateOrder(idCompany, idOrder, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{idOrder}")
    @PreAuthorize("hasAnyRole('ADMIN','ACCOUNTING')")
    public ResponseEntity<GenericResponseDTO> deleteOrder(@PathVariable Integer idCompany,
                                                          @PathVariable Integer idOrder) {
        orderService.deleteOrder(idCompany, idOrder);
        return ResponseEntity.ok(new GenericResponseDTO("order.deleted.success"));
    }
}
