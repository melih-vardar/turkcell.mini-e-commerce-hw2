package com.turkcell.mini_e_commere_hw2.controller;

import com.turkcell.mini_e_commere_hw2.dto.order.OrderListingDto;
import com.turkcell.mini_e_commere_hw2.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController()
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/userId/{userId}")
    public ResponseEntity<String> createOrder(@PathVariable UUID userId) {
        orderService.createOrder(userId);
        return ResponseEntity.ok("order created successfully");
    }
    @PutMapping("/orderId/{orderId}/status")
    public ResponseEntity<String> updateOrderState(@PathVariable Integer orderId) {
        orderService.updateOrderState(orderId);
        return ResponseEntity.ok("order status updated successfully");
    }
    @GetMapping("userId/{userId}")
    public ResponseEntity<List<OrderListingDto>> getAllUserOrders(@PathVariable UUID userId) {
        List<OrderListingDto> orders = orderService.getAllUserOrders(userId);
        return ResponseEntity.ok(orders);
    }
}