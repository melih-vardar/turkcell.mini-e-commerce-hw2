package com.turkcell.mini_e_commere_hw2.controller;

import com.turkcell.mini_e_commere_hw2.dto.order.OrderListingDto;
import com.turkcell.mini_e_commere_hw2.service.application.OrderApplicationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping("/api/v1/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderApplicationService orderApplicationService;

    @PostMapping("/userId/{userId}")
    public ResponseEntity<OrderListingDto> createOrder(@PathVariable UUID userId) {
        return ResponseEntity.ok(orderApplicationService.createOrder(userId));
    }

    @PutMapping("/orderId/{orderId}/status")
    public ResponseEntity<OrderListingDto> updateOrderState(@PathVariable Integer orderId) {
        return ResponseEntity.ok(orderApplicationService.updateOrderState(orderId));
    }

    @GetMapping("userId/{userId}")
    public ResponseEntity<List<OrderListingDto>> getAllUserOrders(@PathVariable UUID userId) {
        List<OrderListingDto> orders = orderApplicationService.getAllUserOrders(userId);
        return ResponseEntity.ok(orders);
    }
}