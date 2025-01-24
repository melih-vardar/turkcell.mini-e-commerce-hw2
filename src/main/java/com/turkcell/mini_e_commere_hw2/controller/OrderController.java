package com.turkcell.mini_e_commere_hw2.controller;

import com.turkcell.mini_e_commere_hw2.dto.order.OrderListingDto;
import com.turkcell.mini_e_commere_hw2.service.application.OrderApplicationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping("/api/v1/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderApplicationService orderApplicationService;

    @PostMapping()
    public ResponseEntity<OrderListingDto> createOrder() {
        return ResponseEntity.ok(orderApplicationService.createOrder());
    }

    @PostMapping("/admin/create-for-user/{userId}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<OrderListingDto> createOrderForUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(orderApplicationService.createOrder(userId));
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderListingDto> updateOrderState(@PathVariable Integer orderId) {
        return ResponseEntity.ok(orderApplicationService.updateOrderState(orderId));
    }

    @GetMapping()
    public ResponseEntity<List<OrderListingDto>> getAllUserOrders() {
        List<OrderListingDto> orders = orderApplicationService.getAllUserOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderListingDto> getOrderById(@PathVariable Integer orderId) {
        return ResponseEntity.ok(orderApplicationService.getOrderById(orderId));
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer orderId) {
        orderApplicationService.deleteOrder(orderId);
        return ResponseEntity.ok().build();
    }
}